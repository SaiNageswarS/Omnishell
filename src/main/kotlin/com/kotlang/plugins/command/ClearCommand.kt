package com.kotlang.plugins.command

import com.kotlang.CommandOutput
import java.nio.file.Path

class ClearCommand: CommandPlugin("clear") {
    override fun execute(workingDir: Path, commandAndArguments: List<String>): CommandOutput {
        return CommandOutput("", "")
    }

}