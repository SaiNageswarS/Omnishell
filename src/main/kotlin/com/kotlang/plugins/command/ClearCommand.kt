package com.kotlang.plugins.command

import com.kotlang.CommandOutput
import com.kotlang.CommandState
import com.kotlang.HistoryItem
import com.kotlang.plugins.CommandPlugin
import com.kotlang.actions.ShellActions
import java.nio.file.Path

class ClearCommand: CommandPlugin("clear") {
    override fun execute(workingDir: Path, commandAndArgsStmt: String,
                         shellActions: ShellActions, historyItem: HistoryItem) {
        shellActions.clearHistory()
        historyItem.output.state = CommandState.SUCCESS
    }

}