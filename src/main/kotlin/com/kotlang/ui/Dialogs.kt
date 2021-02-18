package com.kotlang.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kotlang.Environment

object Dialogs {
    lateinit var toggleEnvironmentDialog: (Boolean) -> Unit

    @Composable
    fun AddDialogs() {
        val showEnvironmentDialog = remember { mutableStateOf(false) }
        toggleEnvironmentDialog = {
            showEnvironmentDialog.value = it
        }
        EnvironmentDialog(showEnvironmentDialog.value)
    }

    @Composable
    private fun EnvironmentDialog(showDialog: Boolean) {
        if (showDialog) {
            val environment = Environment.getEnvironment()

            AlertDialog(
                onDismissRequest = { toggleEnvironmentDialog(false) },
                title = {
                    Text("Environment",
                        color = MaterialTheme.colors.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 10.dp)
                    ) },
                text = {
                    LazyColumn {
                        itemsIndexed(environment.entries.toList()) { _, entry ->
                            Row {
                                Text(entry.key,
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
                },
                buttons = {}
            )
        }
    }
}