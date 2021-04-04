package com.kotlang.shellCommands

import com.kotlang.omnishell.CommandContext
import com.kotlang.omnishell.CommandOutput
import com.kotlang.ui.shell.CommandExecutionCard
import com.kotlang.ui.shell.Shell

class Clear: ShellCommand() {
    override fun isApplicable(command: String): Boolean = command.startsWith("clear")

    override fun execute(cmdInput: CommandContext, shell: Shell): CommandExecutionCard {
        shell.clearHistory()
        return CommandExecutionCard(cmdInput, mutableListOf(), null, CommandOutput.Status.SUCCESS)
    }
}