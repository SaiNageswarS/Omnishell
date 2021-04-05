package com.kotlang.util

import org.apache.logging.log4j.LogManager
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.io.IOException
import java.util.zip.ZipEntry
import java.io.FileInputStream
import java.io.InputStream
import java.nio.file.Path
import java.security.SecureRandom
import java.util.zip.ZipInputStream
import javax.net.ssl.*
import kotlin.system.exitProcess

object HostAgentDownloadUtil {
    private const val downloadUrl = "https://github.com/SaiNageswarS/OmnishellProcessManagerModel/" +
            "releases/download/master/hostManager.zip"

    @JvmStatic
    private val logger = LogManager.getLogger(javaClass)

    private fun getUrlDownloadStream(): InputStream {
        val ctx: SSLContext = SSLContext.getInstance("TLSv1.2")
        ctx.init(null, null, SecureRandom())

        val con = URL(downloadUrl).openConnection() as HttpsURLConnection
        con.sslSocketFactory = ctx.socketFactory
        return con.inputStream
    }

    fun downloadHostManager(dest: Path) {
        logger.info("Downloading host manager from $downloadUrl to $dest")
        try{
            val stream = getUrlDownloadStream()
            Files.copy(stream, Path.of(dest.toString(), "hostManager.zip"), StandardCopyOption.REPLACE_EXISTING)
            unzipFolder(Path.of(dest.toString(), "hostManager.zip"), Path.of(dest.toString(), "hostManager"))
            Files.delete(Paths.get(dest.toString(), "hostManager.zip"))
        } catch (e: Exception) {
            logger.error("Failed getting host manager", e)
            exitProcess(1)
        }
        logger.info("Finished downloading host manager")
    }

    @Throws(IOException::class)
    fun unzipFolder(source: Path, target: Path) {
        ZipInputStream(FileInputStream(source.toFile())).use { zis ->

            // list files in zip
            var zipEntry = zis.nextEntry
            while (zipEntry != null) {
                var isDirectory = false
                // example 1.1
                // some zip stored files and folders separately
                // e.g data/
                //     data/folder/
                //     data/folder/file.txt
                if (zipEntry.name.endsWith("/")) {
                    isDirectory = true
                }
                val newPath: Path = zipSlipProtect(zipEntry, target)
                if (isDirectory) {
                    Files.createDirectories(newPath)
                } else {

                    // example 1.2
                    // some zip stored file path only, need create parent directories
                    // e.g data/folder/file.txt
                    if (newPath.parent != null) {
                        if (Files.notExists(newPath.parent)) {
                            Files.createDirectories(newPath.parent)
                        }
                    }

                    // copy files, nio
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING)
                }
                zipEntry = zis.nextEntry
            }
            zis.closeEntry()
        }
    }

    // protect zip slip attack
    @Throws(IOException::class)
    fun zipSlipProtect(zipEntry: ZipEntry, targetDir: Path): Path {

        // test zip slip vulnerability
        // Path targetDirResolved = targetDir.resolve("../../" + zipEntry.getName());
        val targetDirResolved: Path = targetDir.resolve(zipEntry.name)

        // make sure normalized file still has targetDir as its prefix
        // else throws exception
        val normalizePath: Path = targetDirResolved.normalize()
        if (!normalizePath.startsWith(targetDir)) {
            throw IOException("Bad zip entry: " + zipEntry.name)
        }
        return normalizePath
    }
}
