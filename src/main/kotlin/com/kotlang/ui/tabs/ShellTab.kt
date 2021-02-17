package com.kotlang.ui.tabs

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kotlang.ShellTabData
import com.kotlang.state.ActiveShellState
import com.kotlang.state.WindowState
import com.kotlang.ui.FileTree
import com.kotlang.ui.shell.Shell

@Composable
fun ShellTab(tabIndex: Int) {
    val selectedTabState = mutableStateOf(WindowState.shellStates[tabIndex])
    ActiveShellState.refreshShellTabUICb = { newTabState: ShellTabData ->
        selectedTabState.value = newTabState
    }

    Row(modifier = Modifier.padding(top = 5.dp)) {
        FileTree(selectedTabState.value.currentWorkingDir) {
            selectedTabState.value = ShellTabData(it,
                selectedTabState.value.historyItems)
        }

        Shell(
            selectedTabState.value.currentWorkingDir,
            selectedTabState.value.historyItems)
    }
}