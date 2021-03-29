package com.kotlang.util

import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.io.IOException
import java.util.zip.ZipEntry
import java.io.FileInputStream
import java.nio.file.Path
import java.util.zip.ZipInputStream

object HostManagerUtil {
    private const val downloadUrl = "https://github.com/SaiNageswarS/OmnishellProcessManagerModel/" +
            "releases/download/master/hostManager.zip"

    fun downloadHostManager(dest: String) {
        val stream = URL(downloadUrl).openStream()
        Files.copy(stream, Paths.get("$dest/hostManager.zip"), StandardCopyOption.REPLACE_EXISTING)
        unzipFolder(Path.of("$dest/hostManager.zip"), Path.of("$dest/hostManager"))
        Files.delete(Paths.get("$dest/hostManager.zip"))
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
