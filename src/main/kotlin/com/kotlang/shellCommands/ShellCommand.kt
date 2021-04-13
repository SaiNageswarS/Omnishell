package com.kotlang.shellCommands

import com.kotlang.omnishell.CommandContext
import com.kotlang.ui.shell.CommandExecutionCard
import com.kotlang.ui.shell.Shell

abstract class ShellCommand {
    abstract fun isApplicable(command: String): Boolean
    abstract fun execute(cmdInput: CommandContext, shell: Shell): CommandExecutionCard

    companion object {
        fun getExecutionCard(cmdInput: CommandContext, shell: Shell): CommandExecutionCard {
            val plugins = listOf(ChangeDirectory(), Clear(), EditorCommand(), SshCommand(),
                ExportCommand(), ReplCommand())

            for(plugin in plugins) {
                if (plugin.isApplicable(cmdInput.command)) {
                    return plugin.execute(cmdInput, shell)
                }
            }

            return CommandExecutionCard(cmdInput, shell)
        }
    }
}