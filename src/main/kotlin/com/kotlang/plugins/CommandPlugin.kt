package com.kotlang.plugins

import com.kotlang.ui.shell.CommandOutputCard
import com.kotlang.ui.shell.Shell
import java.nio.file.Path

abstract class CommandPlugin(command: String) {
    private val commandRegex = command.toRegex()

    abstract fun execute(workingDir: Path, commandAndArgsStmt: String,
                         shellActions: Shell, commandOutputCard: CommandOutputCard)

    fun match(inputCmd: String): Boolean {
        return commandRegex.matches(inputCmd)
    }
}