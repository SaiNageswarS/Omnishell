package com.kotlang.ui.shell

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kotlang.util.cloneAndAppend
import java.nio.file.Path

data class HistoryItem(
    val command: String,
    val output: String?
)

@Composable
fun HistoryEntry(historyItem: HistoryItem) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.White,
        modifier = Modifier.fillMaxWidth().padding(10.dp)
    ) {
        //command
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row {
                Text("~  ", color = Color.Blue)
                Text(historyItem.command, color = Color.Green)
            }

            Text(historyItem.output ?: "")
        }
    }
}

@Composable
fun Shell(workingDir: Path, changePath: (Path) -> Unit) {
    val history = remember { mutableStateOf(listOf<HistoryItem>()) }

    ScrollableColumn(
        modifier = Modifier.fillMaxHeight()
            .padding(10.dp)
    ) {
        history.value.forEach {
            HistoryEntry(it)
        }

        Prompt(workingDir, { historyItem ->
            history.value = history.value.cloneAndAppend(historyItem, 50)
        }, changePath)
    }
}
