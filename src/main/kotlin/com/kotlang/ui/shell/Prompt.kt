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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.kotlang.CommandOutput
import com.kotlang.HistoryItem
import com.kotlang.plugins.command.ChangeDirectory
import com.kotlang.plugins.command.ClearCommand
import com.kotlang.plugins.command.CommandPlugin
import com.kotlang.plugins.command.DefaultCommand
import com.kotlang.state.ActiveShellState
import com.kotlang.util.sanitize
import java.nio.file.Path

val commandPlugins = listOf<CommandPlugin>(ChangeDirectory(), ClearCommand())

fun runCommand(workingDir: Path, command: String) {
    val parts = command.split("\\s(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*\$)".toRegex())
    val historyItem = HistoryItem(command, CommandOutput(workingDir))

    for (plugin in commandPlugins) {
        if (plugin.command == parts[0]) {
            historyItem.output = plugin.execute(workingDir, parts)
            ActiveShellState.addCommandOutput(historyItem)
            return
        }
    }

    historyItem.output = DefaultCommand().execute(workingDir, parts)
    ActiveShellState.addCommandOutput(historyItem)
}

@Composable
fun Prompt(workingDir: Path) {
    val command = remember { mutableStateOf("") }
    val commandOutput = mutableStateOf("")
    val commandError = mutableStateOf("")


    val refreshRunningProcessOutput = { output: String, error: String ->
        commandOutput.value = output
        commandError.value = error
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.White,
        modifier = Modifier.fillMaxWidth().padding(10.dp)
    ) {
        TextField(
            value = command.value,
            textStyle = TextStyle(color = Color.Green),
            activeColor = Color.LightGray,
            onValueChange = {
                if (it.endsWith("\n") && !command.value.endsWith("\\")) {
                    runCommand(workingDir, command.value)
                    command.value = ""
                    commandOutput.value = ""
                    commandError.value = ""
                } else {
                    command.value = it
                }
            },
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.Transparent,
            leadingIcon = { Text("~", color = Color.Blue) }
        )
    }
    Text(commandOutput.value.sanitize() ?: "")
    Text(commandError.value.sanitize() ?: "", color = Color.Red)
}