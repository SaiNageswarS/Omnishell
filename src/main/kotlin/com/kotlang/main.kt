package com.kotlang

import androidx.compose.desktop.Window
import androidx.compose.desktop.WindowEvents
import androidx.compose.material.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.kotlang.ui.window.OmnishellWindow
import com.kotlang.ui.window.SplashWindow
import com.kotlang.util.HostAgentDownloadUtil
import com.kotlang.util.VersionString
import com.kotlang.util.VersionVerificationUtil
import org.apache.commons.io.FileUtils
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import javax.imageio.ImageIO

val iconUrl: URL = object {}.javaClass.getResource("/osIcon.png")
val isOldVersion = VersionVerificationUtil().checkIsOldVersion()
val hostManagerPath: Path = Path.of(System.getProperty("user.home"), ".omnishell")

/**
 * returns true if hostManager doesn't exist or is of old version
 */
fun isHostManagerDownloadRequired(): Boolean {
    if (!Files.exists(Path.of(hostManagerPath.toString(), "hostManager"))) {
        return true
    }

    val hostManagerVersionFile = Path.of(hostManagerPath.toString(), "hostManager", "version.json")
    if (!Files.exists(hostManagerVersionFile)) {
        return true
    }

    val objectMapper = ObjectMapper()
    val versionInfo = objectMapper.readValue(hostManagerVersionFile.toFile(), Map::class.java)

    val downloadedVersion = VersionString(versionInfo["version"].toString())
    return VersionVerificationUtil().currentVersion > downloadedVersion
}

fun main() {
    val splashWindow = SplashWindow()
    splashWindow.create()

    if (isHostManagerDownloadRequired()) {
        //delete existing hostManager
        if (Files.exists(Path.of(hostManagerPath.toString(), "hostManager"))) {
            FileUtils.deleteDirectory(Path.of(hostManagerPath.toString(), "hostManager").toFile())
        }
        Files.createDirectories(hostManagerPath)
        HostAgentDownloadUtil.downloadHostManager(hostManagerPath)
    }

    //show splash screen
    Thread.sleep(1000L)

    Window(
        title = "OmniShell",
        events = WindowEvents(
            onClose = { OmnishellWindow.destroy() }
        ),
        icon = ImageIO.read(iconUrl)) {
        MaterialTheme {
            OmnishellWindow.Draw()
        }
        //After content is loaded, close splash screen
        splashWindow.close()
    }
}

