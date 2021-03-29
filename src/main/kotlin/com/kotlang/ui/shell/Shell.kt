package com.kotlang.ui.shell

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kotlang.hostAgent
import com.kotlang.isOldVersion
import com.kotlang.ui.Chip
import com.kotlang.ui.dialogs.EnvironmentDialog
import com.kotlang.ui.window.refreshShell
import java.net.InetAddress
import java.util.*

class Shell(var commandExecutionCards: LinkedList<CommandExecutionCard> = LinkedList<CommandExecutionCard>(),
            var currentWorkingDir: String = hostAgent.getHome(),
            var index: Int = 0) {
    fun addCommandExecution(commandExecution: CommandExecutionCard) {
        commandExecutionCards.addFirst(commandExecution)
        refreshShell()
    }

    fun clearHistory() {
        commandExecutionCards = LinkedList<CommandExecutionCard>()
        refreshShell()
    }

    @Composable
    private fun EnvironmentInfoChips() {
        val hostName = InetAddress.getLocalHost().hostName

        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(vertical = 5.dp).fillMaxWidth().fillMaxHeight(),
        ) {
            Chip(hostName) {}
            Spacer(Modifier.width(30.dp))

            Chip("$currentWorkingDir") {}
            Spacer(Modifier.width(30.dp))

            Chip("Environment") {
                EnvironmentDialog()
            }
        }
    }

    @Composable
    fun Draw(shellStateVersion: Int) {
        val shell = this

        Column(
            modifier = Modifier.fillMaxHeight()
                .padding(10.dp)
        ) {
            if (isOldVersion) {
                val warningColor = Color(red = 223, green = 106, blue = 38)

                Row {
                    Icon(Icons.Default.Warning, contentDescription = "", tint = warningColor)
                    Text(
                        "Update available. Visit https://github.com/SaiNageswarS/Omnishell/releases",
                        color = warningColor,
                    )
                }
            }

            Prompt(shell).Draw()

            LazyColumn(modifier = Modifier.fillMaxHeight(0.88f)) {
                itemsIndexed(commandExecutionCards) { _, outputCard ->
                    outputCard.Draw(shellStateVersion)
                }
            }

            Divider(color = Color.LightGray)
            EnvironmentInfoChips()
        }
    }
}
