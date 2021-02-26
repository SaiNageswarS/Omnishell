package com.kotlang.plugins.command

import com.kotlang.CommandState
import com.kotlang.formatters.ErrorText
import com.kotlang.plugins.CommandPlugin
import com.kotlang.ui.shell.CommandExecutionCard
import com.kotlang.ui.shell.Shell
import com.kotlang.ui.window.changePathUiCb
import com.kotlang.util.getCommandAndArguments
import com.kotlang.util.normalize
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class ChangeDirectory: CommandPlugin("cd\\s.*") {
    override fun execute(workingDir: Path, commandAndArgsStmt: String,
                         shellActions: Shell, executionCard: CommandExecutionCard) {
        val commandAndArguments = commandAndArgsStmt.getCommandAndArguments()

        if (commandAndArguments.size > 1) {
            val destination = Paths.get(commandAndArguments[1])
            val newPath = destination.normalize(workingDir)

            if (Files.notExists(newPath) || !Files.isDirectory(newPath)) {
                executionCard.document.appendWord(
                    ErrorText("Path does not exist."))
                executionCard.refreshState(CommandState.FAILED)
                return
            }
            changePathUiCb(newPath)
        }

        executionCard.refreshState(CommandState.SUCCESS)
    }
}