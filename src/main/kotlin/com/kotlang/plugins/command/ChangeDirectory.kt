package com.kotlang.plugins.command

import com.kotlang.CommandState
import com.kotlang.plugins.CommandPlugin
import com.kotlang.ui.shell.CommandOutputCard
import com.kotlang.ui.shell.Shell
import com.kotlang.ui.window.changePathUiCb
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class ChangeDirectory: CommandPlugin("cd\\s.*") {
    override fun execute(workingDir: Path, commandAndArgsStmt: String,
                         shellActions: Shell, commandOutputCard: CommandOutputCard) {
        val result = commandOutputCard.output
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
                result.error = "Path does not exist."
                result.state = CommandState.FAILED
                commandOutputCard.refreshCommandOutput()
                return
            }
            changePathUiCb(newPath)
        }

        result.state = CommandState.SUCCESS
    }
}