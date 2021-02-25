package com.kotlang.ui.shell

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kotlang.ui.Chip
import com.kotlang.ui.EnvironmentDialog
import com.kotlang.ui.window.refreshShell
import java.net.InetAddress
import java.nio.file.Path

class Shell(var commandExecutionCards: MutableList<CommandExecutionCard> = mutableListOf(),
            var currentWorkingDir: Path = Path.of(System.getProperty("user.home")),
            var index: Int = 0) {

    fun getCommandAtIndex(index: Int): String? {
        if (index < 0 || index >= commandExecutionCards.size) {
            return null
        }

        return commandExecutionCards[index].command
    }

    fun addCommandExecution(commandExecution: CommandExecutionCard) {
        commandExecutionCards.add(0, commandExecution)
        refreshShell()
    }

    fun clearHistory() {
        commandExecutionCards = mutableListOf()
        refreshShell()
    }

    @Composable
    private fun EnvironmentInfoChips() {
        val hostName = InetAddress.getLocalHost().hostName
        val userName = System.getProperty("user.name")

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
