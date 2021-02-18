package com.kotlang.ui.tabs

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kotlang.ShellState
import com.kotlang.actions.ShellActions
import com.kotlang.actions.WindowActions
import com.kotlang.ui.FileTree
import com.kotlang.ui.shell.Shell

class ShellTab(private val windowActions: WindowActions) {
    @Composable
    fun ShellTabWidget(tabIndex: Int) {
        val currentPath = mutableStateOf(windowActions.shellStates[tabIndex].currentWorkingDir)
        val commandHistory = mutableStateOf(windowActions.shellStates[tabIndex].historyItems)

        val refreshShellTabUICb = { newTabState: ShellState ->
            currentPath.value = newTabState.currentWorkingDir
            commandHistory.value = newTabState.historyItems
        }

        val shellActions = ShellActions(windowActions.shellStates[tabIndex], refreshShellTabUICb)
        Row(modifier = Modifier.padding(top = 5.dp)) {
            FileTree(shellActions).FileTreeWidget(currentPath.value)
            Shell(shellActions).ShellWidget(currentPath.value, commandHistory.value)
        }
    }
}