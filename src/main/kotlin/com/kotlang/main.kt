package com.kotlang

import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.kotlang.actions.WindowActions
import com.kotlang.ui.WindowTitle
import com.kotlang.ui.tabs.ShellTab
import com.kotlang.ui.tabs.TabHeader

val shellTabs = mutableListOf(ShellState(index = 0), ShellState(index = 1))

@Composable
fun OmnishellWindow() {
    val selectedTab = remember { mutableStateOf(0) }
    val changeTabUICb = { tabIndex: Int ->
        selectedTab.value = tabIndex
    }

    val windowActions = WindowActions(
        //tabs
        shellTabs,
        changeTabUICb
    )

    val tabHeader = TabHeader(windowActions)
    Column {
        WindowTitle()
        ScrollableTabRow(
            selectedTabIndex = selectedTab.value,
            tabs = {
                //add current tabs
                for (i in windowActions.shellStates.indices) {
                    val windowTitle = windowActions.shellStates[i]
                        .currentWorkingDir.fileName.toString()
                    tabHeader.TabHeader(windowTitle, selectedTab.value == i, i)
                }
                //button to add new tab
                tabHeader.AddNewTabButton()
            }
        )

        ShellTab(windowActions).ShellTabWidget(selectedTab.value)
    }
}

fun main() = Window(title = "OmniShell") {
    MaterialTheme {
        OmnishellWindow()
    }
}
