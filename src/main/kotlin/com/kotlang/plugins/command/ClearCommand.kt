package com.kotlang.plugins.command

import com.kotlang.CommandState
import com.kotlang.plugins.CommandPlugin
import com.kotlang.ui.shell.CommandOutputCard
import com.kotlang.ui.shell.Shell
import java.nio.file.Path

class ClearCommand: CommandPlugin("clear") {
    override fun execute(workingDir: Path, commandAndArgsStmt: String,
                         shellActions: Shell, commandOutputCard: CommandOutputCard) {
        shellActions.clearHistory()
        commandOutputCard.output.state = CommandState.SUCCESS
    }

}