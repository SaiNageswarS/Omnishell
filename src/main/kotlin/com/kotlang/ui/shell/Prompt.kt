package com.kotlang.ui.shell

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
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

    private fun autoComplete(event: androidx.compose.ui.input.key.KeyEvent): Boolean {
        if (event.nativeKeyEvent.id == KeyEvent.KEY_RELEASED)
            return true

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

    private fun handleReturn(event: androidx.compose.ui.input.key.KeyEvent): Boolean {
        if (event.nativeKeyEvent.id == KeyEvent.KEY_RELEASED)
            return true

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

    @Composable
    fun Draw() {
        val commandSuggestions = historyManager.searchHistory(command.value.text)

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
                                autoComplete(it)
                            }
                            Key.Enter -> {
                                handleReturn(it)
                            }
                            else -> false
                        }
                    },
            )
        }
        SearchSuggestions(commandSuggestions,
            modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            command.value = TextFieldValue(it, selection = TextRange(it.length))
        }
    }
}