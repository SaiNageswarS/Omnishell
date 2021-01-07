package com.kotlang.ui.tabs

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kotlang.HistoryItem
import com.kotlang.ShellTabData
import com.kotlang.WindowState
import com.kotlang.ui.FileTree
import com.kotlang.ui.shell.Shell
import com.kotlang.util.cloneAndAppend

@Composable
fun ShellTab(tabIndex: Int) {
    val selectedTabState = mutableStateOf(WindowState.shellStates[tabIndex])
    val refreshTab = { historyItem: HistoryItem ->
        //refresh shell tab
        val newHistoryItems = selectedTabState.value.historyItems
            .cloneAndAppend(historyItem, 50)

        val newTabState = ShellTabData(historyItem.output.path, newHistoryItems)
        WindowState.shellStates[tabIndex] = newTabState
        selectedTabState.value = newTabState
    }

    Row(modifier = Modifier.padding(top = 5.dp)) {
        FileTree(selectedTabState.value.currentWorkingDir) {
            selectedTabState.value = ShellTabData(it,
                selectedTabState.value.historyItems)
        }

        Shell(
            selectedTabState.value.currentWorkingDir,
            selectedTabState.value.historyItems, refreshTab)
    }
}