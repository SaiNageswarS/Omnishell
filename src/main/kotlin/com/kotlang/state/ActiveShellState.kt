package com.kotlang.state

import com.kotlang.HistoryItem
import com.kotlang.ShellTabData
import com.kotlang.util.cloneAndAppend

object ActiveShellState {
    lateinit var refreshShellTabUICb: (ShellTabData) -> Unit
    lateinit var refreshRunningProcessOutput: (String, String) -> Unit

    fun addCommandOutput(historyItem: HistoryItem) {
        val selectedTabIndex = WindowState.selectedTabIndex

        val selectedTab = WindowState.shellStates[selectedTabIndex]
        val newHistoryItems = selectedTab.historyItems
            .cloneAndAppend(historyItem, 50)

        val newTabState = ShellTabData(historyItem.output.path, newHistoryItems)
        WindowState.shellStates[selectedTabIndex] = newTabState
        refreshShellTabUICb(newTabState)
    }

    fun getLastCommand(index: Int): String? {
        val selectedTabIndex = WindowState.selectedTabIndex
        val selectedTab = WindowState.shellStates[selectedTabIndex]

        if (index < 0 || index >= selectedTab.historyItems.size) {
            return null
        }

        return selectedTab.historyItems[index].command
    }
}