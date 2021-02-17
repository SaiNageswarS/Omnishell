package com.kotlang.ui.tabs

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.ExperimentalKeyInput
import androidx.compose.ui.unit.dp
import com.kotlang.state.ShellState
import com.kotlang.state.WindowState
import com.kotlang.ui.FileTree
import com.kotlang.ui.shell.Shell

@ExperimentalKeyInput
@Composable
fun ShellTab(tabIndex: Int) {
    val currentPath = mutableStateOf(WindowState.shellStates[tabIndex].currentWorkingDir)
    val commandHistory = mutableStateOf(WindowState.shellStates[tabIndex].historyItems)

    WindowState.selectedTab.refreshShellTabUICb = { newTabState: ShellState ->
        currentPath.value = newTabState.currentWorkingDir
        commandHistory.value = newTabState.historyItems
    }

    Row(modifier = Modifier.padding(top = 5.dp)) {
        FileTree(currentPath.value)
        Shell(currentPath.value, commandHistory.value)
    }
}