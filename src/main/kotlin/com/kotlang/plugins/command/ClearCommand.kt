package com.kotlang.plugins.command

import com.kotlang.CommandState
import com.kotlang.HistoryItem
import com.kotlang.ShellState
import com.kotlang.plugins.CommandPlugin
import java.nio.file.Path

class ClearCommand: CommandPlugin("clear") {
    override fun execute(workingDir: Path, commandAndArgsStmt: String,
                         shellActions: ShellState, historyItem: HistoryItem) {
        shellActions.clearHistory()
        historyItem.output.state = CommandState.SUCCESS
    }

}