package com.kotlang

import com.kotlang.ui.tabs.refreshShell
import java.nio.file.Path
import java.util.*

enum class CommandState {
    SUCCESS, FAILED, RUNNING
}

data class CommandOutput(
    var output: String = "",
    var error: String = "",
    var state: CommandState = CommandState.RUNNING
)

data class HistoryItem(
    val command: String,
    val output: CommandOutput
)

class ShellState(
    var historyItems: MutableList<HistoryItem> = mutableListOf(),
    var index: Int = 0,
    var shellStateVersion: Int = 0,
    val id: String = UUID.randomUUID().toString()
) {
    var currentWorkingDir: Path = Path.of(System.getProperty("user.home"))
        set(value) {
            field = value
            refreshShell()
        }

    fun getLastCommand(index: Int): String? {
        if (index < 0 || index >= historyItems.size) {
            return null
        }

        return historyItems[index].command
    }

    fun addCommandOutput(historyItem: HistoryItem) {
        historyItems.add(0, historyItem)
        refreshShell()
    }

    fun clearHistory() {
        historyItems = mutableListOf()
        refreshShell()
    }
}