package com.kotlang.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kotlang.ShellState
import com.kotlang.ui.FileTree
import com.kotlang.ui.shell.Shell

var shellRefreshCount = 0
lateinit var refreshShellTabUICb: (Int) -> Unit

fun refreshShell() {
    refreshShellTabUICb(++shellRefreshCount)
}

class ShellTab(private val shellState: ShellState) {
    @Composable
    fun ShellTabWidget() {
        val shellStateVersion = remember { mutableStateOf(shellState.shellStateVersion) }

        refreshShellTabUICb = { refreshCnt: Int ->
            //update ui only if state changed in active tab.
            shellStateVersion.value = refreshCnt
        }

        Row(modifier = Modifier.background(Color(red = 34, green = 51, blue = 68))) {
            FileTree(shellState).FileTreeWidget()
            Shell(shellState).ShellWidget(shellStateVersion.value)
        }
    }
}