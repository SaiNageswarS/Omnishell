package com.kotlang.ui.shell

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kotlang.HistoryItem
import com.kotlang.util.sanitize
import java.nio.file.Path

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

            Text(historyItem.output.output?.sanitize() ?: "")
            Text(historyItem.output.error?.sanitize() ?: "", color = Color.Red)
        }
    }
}

@Composable
fun Shell(workingDir: Path, history: List<HistoryItem>,
          refreshShellTab: (HistoryItem) -> Unit) {

    ScrollableColumn(
        modifier = Modifier.fillMaxHeight()
            .padding(10.dp).background(Color.LightGray)
    ) {
        Prompt(workingDir, refreshShellTab)

        history.forEach {
            HistoryEntry(it)
        }
    }
}
