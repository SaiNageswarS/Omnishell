package com.kotlang.plugins

import com.kotlang.CommandOutput
import com.kotlang.actions.ShellActions
import java.nio.file.Path

abstract class CommandPlugin(command: String) {
    private val commandRegex = command.toRegex()

    abstract fun execute(workingDir: Path, commandAndArgsStmt: String,
                         shellActions: ShellActions): CommandOutput

    fun match(inputCmd: String): Boolean {
        return commandRegex.matches(inputCmd)
    }
}