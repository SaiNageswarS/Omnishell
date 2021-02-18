package com.kotlang.actions

import com.kotlang.HistoryItem
import com.kotlang.ShellState
import com.kotlang.util.cloneAndAppend
import java.nio.file.Path

class ShellActions(
    private val state: ShellState,
    private val refreshShellTabUICb: (ShellState) -> Unit
) {

    fun addCommandOutput(historyItem: HistoryItem) {
        state.historyItems = state.historyItems.cloneAndAppend(historyItem, 50)
        refreshShellTabUICb(state)
    }

    fun getLastCommand(index: Int): String? {
        if (index < 0 || index >= state.historyItems.size) {
            return null
        }

        return state.historyItems[index].command
    }

    fun changePath(newPath: Path) {
        state.currentWorkingDir = newPath
        refreshShellTabUICb(state)
    }

    fun clearHistory() {
        state.historyItems = listOf()
        refreshShellTabUICb(state)
    }
}