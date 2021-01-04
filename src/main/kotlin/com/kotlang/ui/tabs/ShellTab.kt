package com.kotlang.ui.tabs

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kotlang.HistoryItem
import com.kotlang.ShellTabData
import com.kotlang.WindowState
import com.kotlang.ui.FileTree
import com.kotlang.ui.WorkingDirectoryTitle
import com.kotlang.ui.shell.Shell
import com.kotlang.util.cloneAndAppend
import java.nio.file.Path

@Composable
fun ShellTab(tabIndex: Int) {
    val selectedTabState = mutableStateOf(WindowState.shellStates[tabIndex])

    WorkingDirectoryTitle(selectedTabState.value.currentWorkingDir)
    Divider()

    Row(modifier = Modifier.padding(top = 5.dp)) {
        FileTree(selectedTabState.value.currentWorkingDir)

        Shell(
            selectedTabState.value.currentWorkingDir,
            selectedTabState.value.historyItems) { newPath: Path, historyItem: HistoryItem ->
            //refresh shell tab
            val newTabState = ShellTabData(newPath,
                selectedTabState.value.historyItems.cloneAndAppend(historyItem, 50))

            selectedTabState.value = newTabState
            WindowState.shellStates[tabIndex] = newTabState
        }
    }
}