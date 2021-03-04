package com.kotlang

import androidx.compose.desktop.Window
import androidx.compose.material.*
import com.kotlang.ui.window.OmnishellWindow
import com.kotlang.util.VersionVerificationUtil
import java.net.URL
import javax.imageio.ImageIO

private val iconUrl: URL = object {}.javaClass.getResource("/osIcon.png")

val isOldVersion = VersionVerificationUtil().checkIsOldVersion()

fun main() = Window(
    title = "OmniShell",
    icon = ImageIO.read(iconUrl)) {
    MaterialTheme {
        OmnishellWindow().Draw()
    }
}
