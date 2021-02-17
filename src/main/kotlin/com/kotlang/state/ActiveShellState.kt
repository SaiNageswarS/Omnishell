package com.kotlang.state

import com.kotlang.HistoryItem
import com.kotlang.ShellTabData
import com.kotlang.util.cloneAndAppend

object ActiveShellState {
    lateinit var refreshShellTabUICb: (ShellTabData) -> Unit

    fun addCommandOutput(historyItem: HistoryItem) {
        val selectedTabIndex = WindowState.selectedTabIndex

        val selectedTab = WindowState.shellStates[selectedTabIndex]
        val newHistoryItems = selectedTab.historyItems
            .cloneAndAppend(historyItem, 50)

        val newTabState = ShellTabData(historyItem.output.path, newHistoryItems)
        WindowState.shellStates[selectedTabIndex] = newTabState
        refreshShellTabUICb(newTabState)
    }
}