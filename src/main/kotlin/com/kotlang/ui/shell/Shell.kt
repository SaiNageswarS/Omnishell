package com.kotlang.ui.shell

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kotlang.CommandState
import com.kotlang.HistoryItem
import com.kotlang.actions.ShellActions
import com.kotlang.ui.Chip
import com.kotlang.ui.Dialogs
import com.kotlang.ui.PromptIcon
import com.kotlang.util.sanitize
import java.net.InetAddress
import java.nio.file.Path

class Shell(private val shellActions: ShellActions) {
    @Composable
    private fun CommandStateIcon(historyItem: HistoryItem) {
        when(historyItem.output.state) {
            CommandState.RUNNING ->
                Icon(Icons.Default.Refresh, contentDescription = "",
                    modifier = Modifier.size(25.dp))
            CommandState.SUCCESS ->
                Icon(Icons.Filled.CheckCircle, contentDescription = "",
                    modifier = Modifier.size(25.dp), tint = Color.Green)
            CommandState.FAILED ->
                Icon(Icons.Filled.Warning, contentDescription = "",
                    modifier = Modifier.size(25.dp), tint = Color.Red)
        }
    }

    @Composable
    private fun HistoryEntry(historyItem: HistoryItem) {
        Card(
            shape = RoundedCornerShape(8.dp),
            backgroundColor = Color.White,
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            //command
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(0.97f)) {
                        PromptIcon()
                        Text(historyItem.command, color = Color.DarkGray)
                    }
                    CommandStateIcon(historyItem)
                }

                Text(historyItem.output.output.sanitize(), color = Color.DarkGray)
                Text(historyItem.output.error.sanitize(), color = Color.Red)
            }
        }
    }

    @Composable
    private fun EnvironmentInfoChips(currentPath: Path) {
        val hostName = InetAddress.getLocalHost().hostName
        val userName = System.getProperty("user.name")

        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(vertical = 5.dp).fillMaxWidth().fillMaxHeight(),
        ) {
            Chip(hostName) {}
            Spacer(Modifier.width(30.dp))

            Chip("$currentPath") {}
            Spacer(Modifier.width(30.dp))

            Chip("Environment") {
                Dialogs.toggleEnvironmentDialog(true)
            }
        }
    }

    @Composable
    fun ShellWidget(workingDir: Path, history: List<HistoryItem>) {

        Column(
            modifier = Modifier.fillMaxHeight()
                .padding(10.dp)
        ) {
            Prompt(shellActions).PromptWidget(workingDir)

            LazyColumn(modifier = Modifier.fillMaxHeight(0.88f)) {
                itemsIndexed(history) { _, historyItem ->
                    HistoryEntry(historyItem)
                }
            }

            Divider(color = Color.LightGray)
            EnvironmentInfoChips(workingDir)
        }
    }
}
