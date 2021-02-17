package com.kotlang.plugins.command

import com.kotlang.CommandOutput
import com.kotlang.state.WindowState
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class ChangeDirectory: CommandPlugin("cd") {
    override fun execute(workingDir: Path,
                         commandAndArguments: List<String>): CommandOutput {
        var result = CommandOutput("", "", )

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
            WindowState.selectedTab.changePath(newPath)
        }

        return result
    }
}