package com.kotlang.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.net.InetAddress
import java.nio.file.Path

@Composable
fun WindowTitle() {
    TopAppBar {
        Text(
            "OmniShell",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(10.dp))
    }
}