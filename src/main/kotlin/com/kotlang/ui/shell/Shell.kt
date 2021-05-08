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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.kotlang.HostAgent
import com.kotlang.isOldVersion
import com.kotlang.remoting.RemoteTargetManager
import com.kotlang.ui.shell.executionCard.CommandExecutionCard
import com.kotlang.ui.window.OmnishellWindow
import java.util.*

class Shell(var commandExecutionCards: LinkedList<CommandExecutionCard> = LinkedList<CommandExecutionCard>(),
            var index: Int = 0,
            val remoteTargetManager: RemoteTargetManager) {

    private val defaultShell = if (remoteTargetManager.os.indexOf("win") >= 0) "powershell"
        else "/bin/sh"

    var availableOsShells = mutableListOf(defaultShell)
    var osShell = mutableStateOf( defaultShell )

    val hostAgent: HostAgent = HostAgent(remoteTargetManager)
    private val currentWorkingDirState = mutableStateOf(hostAgent.getHome())

    fun addCommandExecution(commandExecution: CommandExecutionCard) {
        commandExecutionCards.addFirst(commandExecution)
        OmnishellWindow.refreshShell()
    }

    fun clearHistory() {
        commandExecutionCards = LinkedList<CommandExecutionCard>()
        OmnishellWindow.refreshShell()
    }

    fun getCurrentWorkingDir(): String = currentWorkingDirState.value
    fun changeDirectory(newPath: String) {
        currentWorkingDirState.value = newPath
    }

    fun destroy() {
        hostAgent.process.destroy()
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
                        fontFamily = FontFamily.Monospace
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

        ShellHeader(shell).Draw(currentWorkingDirState.value)
        Row(modifier = Modifier.background(Color(red = 34, green = 51, blue = 68))) {
            FileTree(shell).FileTreeWidget(currentWorkingDirState.value)
            ShellCommandPallete(shellStateVersion)
        }
    }
}
