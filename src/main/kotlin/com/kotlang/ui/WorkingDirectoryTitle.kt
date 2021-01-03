package com.kotlang.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.nio.file.Path

@Composable
fun WorkingDirectoryTitle(currentPath: Path) {
    Row(modifier = Modifier.padding(20.dp)) {
        Text("PWD: ", color = Color.Green)
        Text(currentPath.toString())
    }
}