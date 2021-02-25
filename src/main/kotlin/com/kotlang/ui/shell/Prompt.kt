package com.kotlang.ui.shell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.kotlang.plugins.CommandPlugin
import com.kotlang.ui.PromptIcon

const val UP_ARROW_KEY = 38
const val DOWN_ARROW_KEY = 40
const val ENTER_KEY = 10

class Prompt(private val shell: Shell) {
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
        val command = remember { mutableStateOf("") }
        val historyIndex = remember { mutableStateOf(-1) }

        Card(
            shape = RoundedCornerShape(8.dp),
            backgroundColor = Color.White,
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            TextField(
                value = command.value,
                textStyle = TextStyle(color = Color.DarkGray),
                onValueChange = { newVal: String -> command.value = newVal },
                modifier = Modifier
                    .background(color = Color.White)
                    .fillMaxWidth()
                    .onKeyEvent {
                        when (it.nativeKeyEvent.keyCode) {
                            UP_ARROW_KEY -> {
                                historyIndex.value += 1
                                shell.getCommandAtIndex(historyIndex.value)?.let { lastCommand ->
                                    command.value = lastCommand
                                }
                                true
                            }
                            DOWN_ARROW_KEY -> {
                                historyIndex.value -= 1
                                shell.getCommandAtIndex(historyIndex.value)?.let { lastCommand ->
                                    command.value = lastCommand
                                }
                                true
                            }
                            ENTER_KEY -> {
                                if (!command.value.endsWith("\\") && command.value.trim().isNotEmpty()) {
                                    runCommand(command.value)
                                    command.value = ""
                                    historyIndex.value = -1
                                    true
                                } else false
                            }
                            else -> false
                        }
                    },
                leadingIcon = { PromptIcon() }
            )
        }
    }
}