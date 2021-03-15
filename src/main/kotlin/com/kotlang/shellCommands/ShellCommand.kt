package com.kotlang.shellCommands

import com.kotlang.omnishell.CommandInput
import com.kotlang.ui.shell.CommandExecutionCard
import com.kotlang.ui.shell.Shell

abstract class ShellCommand {
    abstract fun isApplicable(command: String): Boolean
    abstract fun execute(cmdInput: CommandInput, shell: Shell): CommandExecutionCard

    companion object {
        fun getExecutionCard(cmdInput: CommandInput, shell: Shell): CommandExecutionCard {
            val plugins = listOf(ChangeDirectory(), Clear(), EditorCommand(), SshCommand())

            for(plugin in plugins) {
                if (plugin.isApplicable(cmdInput.commandAndArguments)) {
                    return plugin.execute(cmdInput, shell)
                }
            }

            return CommandExecutionCard(cmdInput)
        }
    }
}