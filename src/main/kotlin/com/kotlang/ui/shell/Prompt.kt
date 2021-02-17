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
import androidx.compose.ui.input.key.ExperimentalKeyInput
import androidx.compose.ui.input.key.keyInputFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.kotlang.CommandOutput
import com.kotlang.HistoryItem
import com.kotlang.plugins.command.ChangeDirectory
import com.kotlang.plugins.command.ClearCommand
import com.kotlang.plugins.command.CommandPlugin
import com.kotlang.plugins.command.DefaultCommand
import com.kotlang.state.WindowState
import java.nio.file.Path

val commandPlugins = listOf<CommandPlugin>(ChangeDirectory(), ClearCommand())
const val UP_ARROW_KEY = 38
const val DOWN_ARROW_KEY = 40
const val ENTER_KEY = 10

fun runCommand(workingDir: Path, command: String) {
    val parts = command.split("\\s(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*\$)".toRegex())
    val historyItem = HistoryItem(command, CommandOutput())

    for (plugin in commandPlugins) {
        if (plugin.command == parts[0]) {
            historyItem.output = plugin.execute(workingDir, parts)
            WindowState.selectedTab.addCommandOutput(historyItem)
            return
        }
    }

    historyItem.output = DefaultCommand().execute(workingDir, parts)
    WindowState.selectedTab.addCommandOutput(historyItem)
}

@ExperimentalKeyInput
@Composable
fun Prompt(workingDir: Path) {
    val command = remember { mutableStateOf("") }
    val historyIndex = remember { mutableStateOf(-1) }

    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.White,
        modifier = Modifier.fillMaxWidth().padding(10.dp)
    ) {
        TextField(
            value = command.value,
            textStyle = TextStyle(color = Color.Green),
            activeColor = Color.LightGray,
            onValueChange = { command.value = it },
            modifier = Modifier.fillMaxWidth().keyInputFilter {
                when (it.key.keyCode) {
                    UP_ARROW_KEY -> {
                        historyIndex.value += 1
                        WindowState.selectedTab.getLastCommand(historyIndex.value)?.let { lastCommand ->
                            command.value = lastCommand
                        }
                        true
                    }
                    DOWN_ARROW_KEY -> {
                        historyIndex.value -= 1
                        WindowState.selectedTab.getLastCommand(historyIndex.value)?.let { lastCommand ->
                            command.value = lastCommand
                        }
                        true
                    }
                    ENTER_KEY -> {
                        if (!command.value.endsWith("\\") && command.value.trim().isNotEmpty()) {
                            runCommand(workingDir, command.value)
                            command.value = ""
                            historyIndex.value = -1
                            true
                        } else false
                    }
                    else -> false
                }
            },
            backgroundColor = Color.Transparent,
            leadingIcon = { Text("~", color = Color.Blue) }
        )
    }
}