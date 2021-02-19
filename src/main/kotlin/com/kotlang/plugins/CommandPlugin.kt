package com.kotlang.plugins

import com.kotlang.HistoryItem
import com.kotlang.ShellState
import java.nio.file.Path

abstract class CommandPlugin(command: String) {
    private val commandRegex = command.toRegex()

    abstract fun execute(workingDir: Path, commandAndArgsStmt: String,
                         shellActions: ShellState, historyItem: HistoryItem)

    fun match(inputCmd: String): Boolean {
        return commandRegex.matches(inputCmd)
    }
}