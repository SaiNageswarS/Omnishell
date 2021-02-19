package com.kotlang.plugins.command

import com.kotlang.CommandOutput
import com.kotlang.CommandState
import com.kotlang.HistoryItem
import com.kotlang.plugins.CommandPlugin
import com.kotlang.actions.ShellActions
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class ChangeDirectory: CommandPlugin("cd\\s.*") {
    override fun execute(workingDir: Path, commandAndArgsStmt: String,
                         shellActions: ShellActions, historyItem: HistoryItem) {
        var result = historyItem.output
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
                return
            }
            shellActions.changePath(newPath)
        }

        result.state = CommandState.SUCCESS
    }
}