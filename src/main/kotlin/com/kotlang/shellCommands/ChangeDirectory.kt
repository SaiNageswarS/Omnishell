package com.kotlang.shellCommands

import com.kotlang.hostAgent
import com.kotlang.omnishell.CommandContext
import com.kotlang.omnishell.CommandOutput
import com.kotlang.ui.shell.CommandExecutionCard
import com.kotlang.ui.shell.Shell
import kotlinx.coroutines.runBlocking

class ChangeDirectory: ShellCommand() {
    override fun isApplicable(command: String): Boolean = command.startsWith("cd")

    override fun execute(cmdInput: CommandContext, shell: Shell): CommandExecutionCard {
        val response = runBlocking { hostAgent.fileSystemClient.changeDirectory(cmdInput) }
        val cmdOutputDoc = mutableListOf<CommandOutput>()

        val cmdOutputStatus = if (response.error != null && response.error.isNotEmpty()) {
            cmdOutputDoc.add(
                CommandOutput.newBuilder().setText(response.error)
                    .setFormat(CommandOutput.TextFormat.ERROR).build()
            )
            CommandOutput.Status.FAILED
        } else {
            shell.changeDirectory(response.outputPath)
            CommandOutput.Status.SUCCESS
        }

        return CommandExecutionCard(cmdInput, cmdOutputDoc, null, cmdOutputStatus)
    }
}