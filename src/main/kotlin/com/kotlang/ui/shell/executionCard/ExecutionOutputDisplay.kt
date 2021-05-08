package com.kotlang.ui.shell.executionCard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.kotlang.omnishell.CommandOutput
import com.kotlang.util.sanitize

class ExecutionOutputDisplay {
    @Composable
    fun Draw(tick: Int, document: List<CommandOutput>) {
        synchronized(this) {
            SelectionContainer {
                Column(
                    modifier = Modifier.padding(horizontal = 0.dp, vertical = 10.dp)
                ) {
                    for (child in document) {
                        when(child.format) {
                            CommandOutput.TextFormat.ERROR -> Text(child.text.sanitize(), color = Color.Red, fontFamily = FontFamily.Monospace)
                            else -> Text(child.text.sanitize(), color = Color.DarkGray, fontFamily = FontFamily.Monospace)
                        }
                    }

                    if (tick > 0) {
                        Text(tick.toString(), color = Color.DarkGray)
                    }
                }
            }
        }
    }
}