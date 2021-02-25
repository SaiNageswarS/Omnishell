package com.kotlang.plugins

import com.kotlang.plugins.command.ChangeDirectory
import com.kotlang.plugins.command.ClearCommand
import com.kotlang.plugins.command.DefaultCommand
import com.kotlang.plugins.command.SshCommand
import com.kotlang.ui.shell.CommandExecutionCard
import com.kotlang.ui.shell.Shell
import java.nio.file.Path

abstract class CommandPlugin(command: String) {
    private val commandRegex = command.toRegex()

    abstract fun execute(workingDir: Path, commandAndArgsStmt: String,
                         shellActions: Shell, commandExecutionCard: CommandExecutionCard)

    fun match(inputCmd: String): Boolean {
        //not working in mac
        return commandRegex.matches(inputCmd)
    }

    companion object {
        val commandPlugins = listOf(ChangeDirectory(), ClearCommand(),
            DefaultCommand())

        fun getPlugin(inputCmd: String): CommandPlugin {
            // not working in mac
//            for (plugin in commandPlugins) {
//                if (plugin.match(inputCmd)) {
//                    return plugin
//                }
//            }

            return when {
                inputCmd.startsWith("cd") -> ChangeDirectory()
                inputCmd.startsWith("clear") -> ClearCommand()
                inputCmd.startsWith("ssh") -> SshCommand()
                else -> DefaultCommand()
            }
        }
    }
}