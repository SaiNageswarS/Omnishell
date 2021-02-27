package com.kotlang.plugins.command

import com.kotlang.CommandState
import com.kotlang.formatters.ErrorText
import com.kotlang.plugins.CommandPlugin
import com.kotlang.ui.shell.CommandExecutionCard
import com.kotlang.ui.shell.Shell
import java.nio.file.Path

class EditorCommand: CommandPlugin() {
    override fun execute(
        workingDir: Path,
        commandAndArgsStmt: String,
        shellActions: Shell,
        executionCard: CommandExecutionCard
    ) {
        executionCard.document.appendWord(ErrorText("An awesome editor is coming soon."))
        executionCard.refreshState(CommandState.FAILED)
    }

    override fun isApplicable(command: String): Boolean {
        return command.startsWith("vi") || command.startsWith("nano")
                || command.startsWith("edit")
    }
}