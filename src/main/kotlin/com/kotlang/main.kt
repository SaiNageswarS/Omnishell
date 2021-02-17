package com.kotlang

import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.input.key.ExperimentalKeyInput
import com.kotlang.state.WindowState
import com.kotlang.ui.WorkingDirectoryTitle
import com.kotlang.ui.tabs.AddNewTabButton
import com.kotlang.ui.tabs.ShellTab
import com.kotlang.ui.tabs.TabHeader

@ExperimentalKeyInput
@Composable
fun OmnishellWindow() {
    val selectedTab = remember { mutableStateOf(0) }
    WindowState.changeTabUICb = { tabIndex: Int ->
        selectedTab.value = tabIndex
    }

    Column {
        WorkingDirectoryTitle(WindowState.selectedTab.currentWorkingDir)

        ScrollableTabRow(
            selectedTabIndex = selectedTab.value,
            tabs = {
                //add current tabs
                for (i in WindowState.shellStates.indices) {
                    val windowTitle = WindowState.shellStates[i]
                        .currentWorkingDir.fileName.toString()
                    TabHeader(windowTitle, selectedTab.value == i, i)
                }
                //button to add new tab
                AddNewTabButton()
            }
        )

        ShellTab(selectedTab.value)
    }
}

@ExperimentalKeyInput
fun main() = Window(title = "OmniShell") {
    MaterialTheme {
        OmnishellWindow()
    }
}
