package com.kotlang.ui.shell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kotlang.hostAgent
import com.kotlang.isOldVersion
import com.kotlang.ui.window.refreshShell
import java.util.*

class Shell(var commandExecutionCards: LinkedList<CommandExecutionCard> = LinkedList<CommandExecutionCard>(),
            var index: Int = 0,
            currentWorkingDir: String = hostAgent.getHome()) {
    private val currentWorkingDirState = mutableStateOf(currentWorkingDir)

    fun addCommandExecution(commandExecution: CommandExecutionCard) {
        commandExecutionCards.addFirst(commandExecution)
        refreshShell()
    }

    fun clearHistory() {
        commandExecutionCards = LinkedList<CommandExecutionCard>()
        refreshShell()
    }

    fun getCurrentWorkingDir(): String = currentWorkingDirState.value
    fun changeDirectory(newPath: String) {
        currentWorkingDirState.value = newPath
    }

    @Composable
    fun ShellCommandPallete(shellStateVersion: Int) {
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
        }
    }

    @Composable
    fun Draw(shellStateVersion: Int) {
        val shell = this

        ShellHeader().Draw("localhost", currentWorkingDirState.value)
        Row(modifier = Modifier.background(Color(red = 34, green = 51, blue = 68))) {
            FileTree(shell).FileTreeWidget(currentWorkingDirState.value)
            ShellCommandPallete(shellStateVersion)
        }
    }
}
