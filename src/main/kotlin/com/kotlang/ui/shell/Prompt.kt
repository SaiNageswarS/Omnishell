package com.kotlang.ui.shell

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.kotlang.plugins.AutoCompletePlugin
import com.kotlang.plugins.CommandPlugin
import com.kotlang.ui.PromptIcon
import java.awt.event.KeyEvent

const val UP_ARROW_KEY = 38
const val DOWN_ARROW_KEY = 40

class Prompt(private val shell: Shell) {
    private var autoCompleteInvocations = 0
    private var oldSearchString = ""
    private var searchResult = listOf<String>()

    private fun runCommand(command: String) {
        val cmdRes = CommandExecutionCard(command)
        shell.addCommandExecution(cmdRes)

        val plugin = CommandPlugin.getPlugin(command)
        Thread {
            //wait for initialization of command card UI callbacks
            Thread.sleep(500)
            plugin.execute(shell.currentWorkingDir, command, shell, cmdRes)
        }.start()
    }

    @Composable
    fun Draw() {
        val command = remember { mutableStateOf(TextFieldValue("")) }
        val historyIndex = remember { mutableStateOf(-1) }

        Card(
            shape = RoundedCornerShape(8.dp),
            backgroundColor = Color.White,
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            TextField(
                value = command.value,
                textStyle = TextStyle(color = Color.DarkGray),
                onValueChange = { newVal: TextFieldValue -> command.value = newVal },
                placeholder = { Text("Run Command here. Press Tab for Auto-Complete") },
                leadingIcon = { PromptIcon() },
                modifier = Modifier
                    .fillMaxWidth()
                    .onPreviewKeyEvent {
                        when(it.key) {
                            Key.Tab -> {
                                if (it.nativeKeyEvent.id == KeyEvent.KEY_RELEASED)
                                    return@onPreviewKeyEvent true

                                if (command.value.text.trim().isEmpty())
                                    return@onPreviewKeyEvent true

                                if (command.value.text.trim() != oldSearchString) {
                                    searchResult = AutoCompletePlugin.autoComplete(
                                        shell.currentWorkingDir, command.value.text)
                                    autoCompleteInvocations = -1
                                }

                                if (searchResult.isNotEmpty()) {
                                    autoCompleteInvocations += 1
                                    val idx = autoCompleteInvocations % searchResult.size
                                    command.value = TextFieldValue(searchResult[idx],
                                        selection = TextRange(searchResult[idx].length))
                                    oldSearchString = command.value.text
                                }
                                true
                            }
                            Key.Enter -> {
                                if (it.nativeKeyEvent.id == KeyEvent.KEY_RELEASED)
                                    return@onPreviewKeyEvent true

                                val cmdText = command.value.text
                                if (!cmdText.endsWith("\\") && cmdText.trim().isNotEmpty()) {
                                    runCommand(cmdText)
                                    command.value = TextFieldValue("")
                                    oldSearchString = ""
                                    searchResult = listOf()
                                    historyIndex.value = -1
                                    true
                                } else false
                            }
                            else -> false
                        }
                    }
                    .onKeyEvent {
                        when (it.nativeKeyEvent.keyCode) {
                            UP_ARROW_KEY -> {
                                if (it.nativeKeyEvent.id == KeyEvent.KEY_RELEASED)
                                    return@onKeyEvent true

                                historyIndex.value += 1
                                shell.getCommandAtIndex(historyIndex.value)?.let { lastCommand ->
                                    command.value = TextFieldValue(lastCommand)
                                }
                                true
                            }
                            DOWN_ARROW_KEY -> {
                                if (it.nativeKeyEvent.id == KeyEvent.KEY_RELEASED)
                                    return@onKeyEvent true

                                historyIndex.value -= 1
                                shell.getCommandAtIndex(historyIndex.value)?.let { lastCommand ->
                                    command.value = TextFieldValue(lastCommand)
                                }
                                true
                            }
                            else -> false
                        }
                    },
            )
        }
    }
}