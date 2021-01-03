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
import com.kotlang.util.runCommand
import java.nio.file.Path


@Composable
fun Prompt(workingDir: Path, appendHistory: (HistoryItem) -> Unit,
           changePath: (Path) -> Unit) {
    val command = remember { mutableStateOf("") }

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
                    val historyItem = HistoryItem(command.value,
                        command.value.runCommand(workingDir, changePath))
                    appendHistory(historyItem)
                    command.value = ""
                } else {
                    command.value = it
                }
            },
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color.Transparent,
            leadingIcon = { Text("~", color = Color.Blue) }
        )
    }
}