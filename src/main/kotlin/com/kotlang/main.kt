package com.kotlang

import androidx.compose.desktop.Window
import androidx.compose.material.*
import com.kotlang.ui.window.OmnishellWindow
import com.kotlang.util.VersionString
import kong.unirest.Unirest
import kong.unirest.json.JSONObject
import java.net.URL
import javax.imageio.ImageIO

private val iconUrl: URL = object {}.javaClass.getResource("/osIcon.png")

private val currentVersion: VersionString = VersionString("0.0.3")
private val os: String = System.getProperty("os.name").split(" ").first()

val isOldVersion = checkIsOldVersion()

fun checkIsOldVersion(): Boolean {
    try {
        val releases = Unirest.get("https://api.github.com/repos/SaiNageswarS/Omnishell/releases")
            .asJson().body
        val osTags = releases.array.map { (it as JSONObject).get("tag_name") as String }
            .filter { it.startsWith(os) }
        val availableVersion = VersionString(osTags[0].split("-").last())
        return currentVersion < availableVersion
    } catch (e: Exception) {
        return true
    }
}

fun main() = Window(
    title = "OmniShell",
    icon = ImageIO.read(iconUrl)) {
    MaterialTheme {
        OmnishellWindow().Draw()
    }
}
