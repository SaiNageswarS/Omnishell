package com.kotlang

import androidx.compose.desktop.Window
import androidx.compose.desktop.WindowEvents
import androidx.compose.material.*
import com.kotlang.ui.window.OmnishellWindow
import com.kotlang.ui.window.SplashWindow
import com.kotlang.util.HostManagerUtil
import com.kotlang.util.VersionVerificationUtil
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import javax.imageio.ImageIO

val iconUrl: URL = object {}.javaClass.getResource("/osIcon.png")
val isOldVersion = VersionVerificationUtil().checkIsOldVersion()
val hostManagerPath: Path = Path.of(System.getProperty("user.home"), ".omnishell")

fun main() {
    val splashWindow = SplashWindow()
    splashWindow.create()

    if (!Files.exists(Path.of("$hostManagerPath/hostManager"))) {
        Files.createDirectories(hostManagerPath)
        HostManagerUtil.downloadHostManager(hostManagerPath)
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

