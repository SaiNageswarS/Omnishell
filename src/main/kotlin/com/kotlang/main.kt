package com.kotlang

import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kotlang.ui.FileTree
import com.kotlang.ui.shell.Shell
import com.kotlang.ui.WorkingDirectoryTitle
import java.nio.file.Path

fun main() = Window(title = "OmniShell") {
    val currentPath = remember { mutableStateOf(Path.of(System.getProperty("user.home"))) }
    MaterialTheme {
        Column(modifier = Modifier.background(Color.LightGray)) {
            WorkingDirectoryTitle(currentPath.value)
            Divider()

            Row(modifier = Modifier.padding(top = 5.dp)) {
                FileTree(currentPath.value)

                Shell(currentPath.value) {
                    currentPath.value = it
                }
            }
        }
    }
}
