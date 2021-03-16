package com.kotlang.ui.shell

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.kotlang.hostAgent
import com.kotlang.omnishell.*
import com.kotlang.shellCommands.ShellCommand
import com.kotlang.ui.PromptIcon
import com.kotlang.ui.SearchSuggestions
import java.awt.event.KeyEvent
import kotlinx.coroutines.*

//const val UP_ARROW_KEY = 38
//const val DOWN_ARROW_KEY = 40

class Prompt(private val shell: Shell) {
    private var oldSearchString = ""
    private var searchResult = listOf<String>()
    private val command = mutableStateOf(TextFieldValue(""))
    private val suggestionIdx = mutableStateOf(-1)
    private val commandSuggestions = mutableStateOf(listOf<String>())


    private fun runCommand(command: String) {
        val cmd = CommandContext.newBuilder().setCommand(command.replace("sudo", "sudo -S "))
            .setWorkingDir(shell.currentWorkingDir.toString()).build()
        val cmdRes = ShellCommand.getExecutionCard(cmd, shell)

        shell.addCommandExecution(cmdRes)
        runBlocking {
            hostAgent.historyManagerClient.addToHistory(
                HistoryEntry.newBuilder().setCommand(command).build()
            )
        }
    }

    private fun autoComplete(): Boolean {
        if (suggestionIdx.value == -1 && commandSuggestions.value.isNotEmpty()) {
            val firstSuggestion = commandSuggestions.value[0]
            command.value = TextFieldValue(firstSuggestion,
                selection = TextRange(firstSuggestion.length))
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
//        if (commandSuggestions.value.isEmpty() &&
//            suggestionIdx.value + increment in historyManager.history.indices) {
//            val selectedCommand = historyManager.history[suggestionIdx.value + increment]
//            command.value = TextFieldValue(selectedCommand, selection = TextRange(selectedCommand.length))
//            suggestionIdx.value = suggestionIdx.value + increment
//        }

        if (suggestionIdx.value + increment in commandSuggestions.value.indices) {
            val selectedCommand = commandSuggestions.value[suggestionIdx.value + increment]
            command.value = TextFieldValue(selectedCommand, selection = TextRange(selectedCommand.length))
            suggestionIdx.value = suggestionIdx.value + increment
        }
    }

    @ExperimentalCoroutinesApi
    @Composable
    fun Draw() {
        val scope = rememberCoroutineScope()

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
                    scope.launch {
                        val autoCompleteSuggestions = async {
                            hostAgent.autoCompleteClient.autoComplete(
                                CommandContext.newBuilder().setCommand(command.value.text)
                                    .setMaxSuggestions(3).setWorkingDir(shell.currentWorkingDir.toString()).build()
                            ).suggestionsList
                        }
                        val historySuggestions = async {
                            hostAgent.historyManagerClient.searchHistory(
                                HistoryQuery.newBuilder().setPrefix(command.value.text)
                                    .setLimit(5).build()
                            ).searchResultList
                        }

                        listOf(autoCompleteSuggestions, historySuggestions).awaitAll()
                        val suggestions = autoCompleteSuggestions.getCompleted() + historySuggestions.getCompleted()
                        suggestionIdx.value = -1
                        commandSuggestions.value = suggestions.distinct()
                    }
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