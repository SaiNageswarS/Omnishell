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
import com.kotlang.CommandOutput
import com.kotlang.HistoryItem
import com.kotlang.ShellState
import com.kotlang.plugins.command.ChangeDirectory
import com.kotlang.plugins.command.ClearCommand
import com.kotlang.plugins.command.DefaultCommand
import com.kotlang.ui.PromptIcon

val commandPlugins = listOf(ChangeDirectory(), ClearCommand(),
    DefaultCommand())

const val UP_ARROW_KEY = 38
const val DOWN_ARROW_KEY = 40
const val ENTER_KEY = 10

class Prompt(private val shellState: ShellState) {
    private fun runCommand(command: String) {
        val cmdRes = HistoryItem(command, CommandOutput())

        for (plugin in commandPlugins) {
            if (plugin.match(command)) {
                Thread { plugin.execute(shellState.currentWorkingDir, command, shellState, cmdRes) }.start()
                shellState.addCommandOutput(cmdRes)
                return
            }
        }
    }

    @Composable
    fun PromptWidget() {
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
                                shellState.getLastCommand(historyIndex.value)?.let { lastCommand ->
                                    command.value = lastCommand
                                }
                                true
                            }
                            DOWN_ARROW_KEY -> {
                                historyIndex.value -= 1
                                shellState.getLastCommand(historyIndex.value)?.let { lastCommand ->
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