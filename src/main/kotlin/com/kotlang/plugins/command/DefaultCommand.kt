package com.kotlang.plugins.command

import com.kotlang.CommandOutput
import com.kotlang.util.gobbleStream
import java.nio.file.Path

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

            val outputBuffer = StringBuffer()
            val errorBuffer = StringBuffer()

            val streamTasks = listOf(
                gobbleStream(proc.inputStream, outputBuffer),
                gobbleStream(proc.errorStream, errorBuffer))

            proc.waitFor()
            streamTasks.forEach { it.join() }

            commandOutput.output = outputBuffer.toString()
            commandOutput.error = errorBuffer.toString()
        } catch (e: Exception) {
            commandOutput.error = e.message
            e.printStackTrace()
        }

        return commandOutput
    }
}
