package com.kotlang.plugins.command

import com.kotlang.CommandOutput
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class ChangeDirectory: CommandPlugin("cd") {
    override fun execute(workingDir: Path,
                         commandAndArguments: List<String>): CommandOutput {
        var result = CommandOutput(workingDir, "", "", )

        if (commandAndArguments.size > 1) {
            val destination = Paths.get(commandAndArguments[1])

            result.path = if (destination.isAbsolute)
                destination
            else
                FileSystems.getDefault()
                    .getPath(workingDir.toString(), destination.toString())
                    .normalize().toAbsolutePath()

            if (Files.notExists(result.path) || !Files.isDirectory(result.path)) {
                result = CommandOutput(workingDir, "", "Path does not exist.")
            }
        }

        return result
    }
}