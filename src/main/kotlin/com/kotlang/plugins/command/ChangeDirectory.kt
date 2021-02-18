package com.kotlang.plugins.command

import com.kotlang.CommandOutput
import com.kotlang.plugins.CommandPlugin
import com.kotlang.actions.ShellActions
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class ChangeDirectory: CommandPlugin("cd\\s.*") {
    override fun execute(workingDir: Path, commandAndArgsStmt: String,
                         shellActions: ShellActions): CommandOutput {
        var result = CommandOutput("", "", )
        val commandAndArguments = commandAndArgsStmt.split("\\s(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*\$)".toRegex())

        if (commandAndArguments.size > 1) {
            val destination = Paths.get(commandAndArguments[1])

            val newPath = if (destination.isAbsolute)
                destination
            else
                FileSystems.getDefault()
                    .getPath(workingDir.toString(), destination.toString())
                    .normalize().toAbsolutePath()

            if (Files.notExists(newPath) || !Files.isDirectory(newPath)) {
                result = CommandOutput( "", "Path does not exist.")
                return result
            }
            shellActions.changePath(newPath)
        }

        return result
    }
}