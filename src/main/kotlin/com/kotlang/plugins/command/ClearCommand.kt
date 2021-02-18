package com.kotlang.plugins.command

import com.kotlang.CommandOutput
import com.kotlang.plugins.CommandPlugin
import com.kotlang.actions.ShellActions
import java.nio.file.Path

class ClearCommand: CommandPlugin("clear") {
    override fun execute(workingDir: Path, commandAndArgsStmt: String,
                         shellActions: ShellActions): CommandOutput {
        shellActions.clearHistory()
        return CommandOutput("", "")
    }

}