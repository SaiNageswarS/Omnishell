package com.kotlang

import androidx.compose.desktop.Window
import androidx.compose.material.*
import com.kotlang.ui.window.OmnishellWindow


fun main() = Window(title = "OmniShell") {
    MaterialTheme {
        OmnishellWindow().Draw()
    }
}
