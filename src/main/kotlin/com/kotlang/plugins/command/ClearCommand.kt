package com.kotlang.plugins.command

import com.kotlang.CommandState
import com.kotlang.plugins.CommandPlugin
import com.kotlang.ui.shell.CommandExecutionCard
import com.kotlang.ui.shell.Shell
import java.nio.file.Path

class ClearCommand: CommandPlugin("clear") {
    override fun execute(workingDir: Path, commandAndArgsStmt: String,
                         shellActions: Shell, executionCard: CommandExecutionCard) {
        shellActions.clearHistory()
        executionCard.refreshState(CommandState.SUCCESS)
    }

}