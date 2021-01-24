package com.kotlang.plugins.command

import com.kotlang.CommandOutput
import java.nio.file.Path
import java.util.concurrent.TimeUnit

class DefaultCommand {
    fun execute(workingDir: Path, commandAndArguments: List<String>): CommandOutput {
        val commandOutput = CommandOutput(workingDir, "", "")

        try {
            val shellCommand = commandAndArguments.joinToString(separator = " ")
            val proc = ProcessBuilder("/bin/sh", "-c", shellCommand)
                .directory(workingDir.toFile())
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

            proc.waitFor(60, TimeUnit.MINUTES)
            commandOutput.output = proc.inputStream.bufferedReader().readText()
            commandOutput.error = proc.errorStream.bufferedReader().readText()
        } catch (e: Exception) {
            commandOutput.error = e.message
            e.printStackTrace()
        }

        return commandOutput
    }
}
