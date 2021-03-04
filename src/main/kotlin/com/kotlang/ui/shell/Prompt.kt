package com.kotlang.ui.shell

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import com.kotlang.historyManager
import com.kotlang.plugins.AutoCompletePlugin
import com.kotlang.plugins.CommandPlugin
import com.kotlang.ui.PromptIcon
import com.kotlang.ui.SearchSuggestions
import java.awt.event.KeyEvent

//const val UP_ARROW_KEY = 38
//const val DOWN_ARROW_KEY = 40

class Prompt(private val shell: Shell) {
    private var autoCompleteInvocations = 0
    private var oldSearchString = ""
    private var searchResult = listOf<String>()
    private val command = mutableStateOf(TextFieldValue(""))
    private val suggestionIdx = mutableStateOf(-1)
    private val commandSuggestions = mutableStateOf(listOf<String>())


    private fun runCommand(command: String) {
        val cmdRes = CommandExecutionCard(command)
        shell.addCommandExecution(cmdRes)
        historyManager.addToHistory(command)

        val plugin = CommandPlugin.getPlugin(command)
        Thread {
            //wait for initialization of command card UI callbacks
            Thread.sleep(500)
            plugin.execute(shell.currentWorkingDir, command.replace("sudo", "sudo -S "),
                shell, cmdRes)
        }.start()
    }

    private fun autoComplete(): Boolean {
        if (command.value.text.trim().isEmpty())
            return true

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
        return true
    }

    private fun handleReturn(): Boolean {
        val cmdText = command.value.text
        return if (!cmdText.endsWith("\\") && cmdText.trim().isNotEmpty()) {
            runCommand(cmdText)
            command.value = TextFieldValue("")
            oldSearchString = ""
            searchResult = listOf()
            true
        } else
            false
    }

    private fun selectSuggestion(increment: Int) {
        if (commandSuggestions.value.isEmpty() &&
            suggestionIdx.value + increment in historyManager.history.indices) {
            val selectedCommand = historyManager.history[suggestionIdx.value + increment]
            command.value = TextFieldValue(selectedCommand, selection = TextRange(selectedCommand.length))
            suggestionIdx.value = suggestionIdx.value + increment
        }

        if (suggestionIdx.value + increment in commandSuggestions.value.indices) {
            val selectedCommand = commandSuggestions.value[suggestionIdx.value + increment]
            command.value = TextFieldValue(selectedCommand, selection = TextRange(selectedCommand.length))
            suggestionIdx.value = suggestionIdx.value + increment
        }
    }

    @Composable
    fun Draw() {
        Card(
            shape = RoundedCornerShape(8.dp),
            backgroundColor = Color.White,
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            TextField(
                value = command.value,
                textStyle = TextStyle(color = Color.DarkGray),
                onValueChange = { newVal: TextFieldValue ->
                    command.value = newVal
                    suggestionIdx.value = -1
                    commandSuggestions.value = historyManager.searchHistory(command.value.text)
                },
                placeholder = { Text("Run Command here. Press Tab for Auto-Complete") },
                leadingIcon = { PromptIcon() },
                modifier = Modifier
                    .fillMaxWidth()
                    .onPreviewKeyEvent {
                        if (it.nativeKeyEvent.id == KeyEvent.KEY_RELEASED)
                            return@onPreviewKeyEvent false

                        when(it.key) {
                            Key.Tab -> {
                                autoComplete()
                            }
                            Key.Enter -> {
                                handleReturn()
                            }
                            Key.DirectionDown -> {
                                selectSuggestion(1)
                                true
                            }
                            Key.DirectionUp -> {
                                selectSuggestion(-1)
                                true
                            }
                            else -> false
                        }
                    },
            )
        }
        SearchSuggestions(commandSuggestions.value, suggestionIdx.value,
            modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            command.value = TextFieldValue(it, selection = TextRange(it.length))
        }
    }
}