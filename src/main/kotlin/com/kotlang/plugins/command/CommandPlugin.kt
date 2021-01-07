package com.kotlang.plugins.command

import com.kotlang.CommandOutput
import java.nio.file.Path

abstract class CommandPlugin(val command: String) {
    abstract fun execute(workingDir: Path, commandAndArguments: List<String>): CommandOutput
}