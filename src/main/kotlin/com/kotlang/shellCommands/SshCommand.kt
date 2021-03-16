package com.kotlang.shellCommands

import com.kotlang.omnishell.CommandContext
import com.kotlang.omnishell.CommandOutput
import com.kotlang.ui.shell.CommandExecutionCard
import com.kotlang.ui.shell.Shell

class SshCommand: ShellCommand() {
    override fun isApplicable(command: String): Boolean = command.startsWith("ssh")

    override fun execute(cmdInput: CommandContext, shell: Shell): CommandExecutionCard {
        val err = CommandOutput.newBuilder().setText("An awesome editor is coming soon.")
            .setFormat(CommandOutput.TextFormat.ERROR).build()

        return CommandExecutionCard(cmdInput, mutableListOf(err),
            CommandOutput.Status.FAILED)
    }
}