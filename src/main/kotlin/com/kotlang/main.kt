package com.kotlang

import androidx.compose.desktop.AppManager
import androidx.compose.desktop.Window
import androidx.compose.desktop.WindowEvents
import androidx.compose.material.*
import com.google.protobuf.ProtocolStringList
import com.kotlang.omnishell.Environment
import com.kotlang.omnishell.EnvironmentRequest
import com.kotlang.ui.window.OmnishellWindow
import com.kotlang.ui.window.SplashWindow
import com.kotlang.util.HostManagerUtil
import com.kotlang.util.VersionVerificationUtil
import kotlinx.coroutines.runBlocking
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import javax.imageio.ImageIO

val iconUrl: URL = object {}.javaClass.getResource("/osIcon.png")
val isOldVersion = VersionVerificationUtil().checkIsOldVersion()
val hostManagerPath: String = System.getProperty("user.home")

fun main() {
    val splashWindow = SplashWindow()
    splashWindow.create()

    if (!Files.exists(Path.of("$hostManagerPath/hostManager"))) {
        HostManagerUtil.downloadHostManager(hostManagerPath)
    }
    //booting hostAgent while splash screen is running
    runBlocking {
        hostAgent.environmentClient.getEnvironment(EnvironmentRequest.getDefaultInstance()).envList
    }
    //show splash screen
    Thread.sleep(1000L)

    Window(
        title = "OmniShell",
        events = WindowEvents(
            onClose = { hostAgent.process.destroy() }
        ),
        icon = ImageIO.read(iconUrl)) {
        MaterialTheme {
            OmnishellWindow().Draw()
        }
        //After content is loaded, close splash screen
        splashWindow.close()
    }
}

