package com.kotlang.ui

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kotlang.Environment

fun EnvironmentDialog() = Window(title = "Environment") {
    val environment = Environment.getEnvironment()

    LazyColumn {
        itemsIndexed(environment.entries.toList()) { _, entry ->
            Row {
                Text(
                    entry.key,
                    modifier = Modifier.padding(horizontal = 5.dp),
                    style = TextStyle(
                        color = MaterialTheme.colors.primaryVariant,
                        fontWeight = FontWeight.SemiBold,
                    )
                )
                Text(entry.value)
            }
            Divider()
        }
    }
}
