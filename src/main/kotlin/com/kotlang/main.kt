package com.kotlang

import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.kotlang.ui.WorkingDirectoryTitle
import com.kotlang.ui.tabs.AddTabItem
import com.kotlang.ui.tabs.ShellTab
import com.kotlang.ui.tabs.ShellTabRowItem

object WindowState {
    val shellStates = mutableListOf(ShellTabData(), ShellTabData())
}

fun main() = Window(title = "OmniShell") {
    val selectedTab = remember { mutableStateOf(0) }
    val changeTab = { tabIndex: Int -> selectedTab.value = tabIndex }
    val closeTab = { tabIndex: Int ->
        WindowState.shellStates.removeAt(tabIndex)
        selectedTab.value = if (selectedTab.value == tabIndex) 0 else selectedTab.value
    }

    MaterialTheme {
        Column {
            val selectedTabState = WindowState.shellStates[selectedTab.value]
            WorkingDirectoryTitle(selectedTabState.currentWorkingDir)

            ScrollableTabRow(
                selectedTabIndex = selectedTab.value,
                tabs = {
                    for (i in WindowState.shellStates.indices) {
                        val windowTitle = WindowState.shellStates[i]
                            .currentWorkingDir.fileName.toString()
                        ShellTabRowItem(windowTitle,
                            selectedTab.value == i, i, closeTab, changeTab)
                    }
                    AddTabItem {
                        WindowState.shellStates.add(ShellTabData())
                        selectedTab.value = WindowState.shellStates.size - 1;
                    }
                }
            )

            ShellTab(selectedTab.value)
        }
    }
}
