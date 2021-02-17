package com.kotlang.state

import com.kotlang.HistoryItem
import com.kotlang.util.cloneAndAppend
import java.nio.file.Path
import java.util.*

class ShellState(
    private val id: String = UUID.randomUUID().toString(),
    var index: Int = 0,
    var currentWorkingDir: Path = Path.of(System.getProperty("user.home")),
    var historyItems: List<HistoryItem> = listOf()
) {

    lateinit var refreshShellTabUICb: (ShellState) -> Unit

    fun addCommandOutput(historyItem: HistoryItem) {
        historyItems = historyItems.cloneAndAppend(historyItem, 50)
        refreshShellTabUICb(this)
    }

    fun getLastCommand(index: Int): String? {
        if (index < 0 || index >= historyItems.size) {
            return null
        }

        return historyItems[index].command
    }

    fun changePath(newPath: Path) {
        currentWorkingDir = newPath
        refreshShellTabUICb(this)
    }

    override fun equals(other: Any?): Boolean {
        return (other is ShellState) &&
                id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}