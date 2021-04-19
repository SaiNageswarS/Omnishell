package com.kotlang.ui.dialogs

import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kotlang.omnishell.EnvironmentRequest
import com.kotlang.ui.shell.Shell
import kotlinx.coroutines.runBlocking

fun EnvironmentDialog(shell: Shell) = Window(title = "Environment") {
    val environment = runBlocking { shell.hostAgent.environmentClient.getEnvironment(
        EnvironmentRequest.getDefaultInstance()).envList }

    LazyColumn {
        itemsIndexed(environment) { _, entry ->
            Row {
                Text(
                    entry.split("=")[0],
                    modifier = Modifier.padding(horizontal = 5.dp),
                    color = MaterialTheme.colors.primaryVariant,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace
                )
                Text(entry.split("=")[1], fontFamily = FontFamily.Monospace)
            }
            Divider()
        }
    }
}
