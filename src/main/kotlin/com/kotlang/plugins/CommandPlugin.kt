package com.kotlang.plugins

import com.kotlang.plugins.command.*
import com.kotlang.ui.shell.CommandExecutionCard
import com.kotlang.ui.shell.Shell
import java.nio.file.Path

abstract class CommandPlugin {
    abstract fun execute(workingDir: Path, commandAndArgsStmt: String,
                         shellActions: Shell, executionCard: CommandExecutionCard)

    abstract fun isApplicable(command: String): Boolean

    companion object {
        private val commandPlugins = listOf(ChangeDirectory(), ClearCommand(),
            SshCommand(), EditorCommand(),
            DefaultCommand())

        fun getPlugin(inputCmd: String): CommandPlugin {
            for (plugin in commandPlugins) {
                if (plugin.isApplicable(inputCmd)) {
                    return plugin
                }
            }
            return DefaultCommand()
        }
    }
}
