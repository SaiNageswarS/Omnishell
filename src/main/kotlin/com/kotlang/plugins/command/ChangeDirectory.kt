package com.kotlang.plugins.command

import com.kotlang.CommandState
import com.kotlang.formatters.ErrorText
import com.kotlang.hostAgent
import com.kotlang.omnishell.CommandContext
import com.kotlang.plugins.CommandPlugin
import com.kotlang.ui.shell.CommandExecutionCard
import com.kotlang.ui.shell.Shell
import com.kotlang.ui.window.changePathUiCb
import kotlinx.coroutines.runBlocking
import java.nio.file.Path

class ChangeDirectory: CommandPlugin() {
    override fun execute(workingDir: Path, commandAndArgsStmt: String,
                         shellActions: Shell, executionCard: CommandExecutionCard) {
        val output = runBlocking { hostAgent.fileSystemClient.changeDirectory(
            CommandContext.newBuilder().setWorkingDir(workingDir.toString())
                .setCommand(commandAndArgsStmt).build()
        ) }

        if (output.error != null && output.error.isNotEmpty()) {
            executionCard.document.appendWord(
                ErrorText(output.error))
            executionCard.refreshState(CommandState.FAILED)
            return
        }
        changePathUiCb(Path.of(output.outputPath))
        executionCard.refreshState(CommandState.SUCCESS)
    }

    override fun isApplicable(command: String): Boolean = command.startsWith("cd")
}
