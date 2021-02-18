package com.kotlang.plugins.command

import com.kotlang.CommandOutput
import com.kotlang.plugins.CommandPlugin
import com.kotlang.actions.ShellActions
import com.kotlang.util.gobbleStream
import java.nio.file.Path

class DefaultCommand: CommandPlugin(".*") {
    override fun execute(workingDir: Path, commandAndArgsStmt: String,
                shellActions: ShellActions): CommandOutput {
        val commandOutput = CommandOutput("", "")

        try {
            val procBuilder = ProcessBuilder("/bin/sh", "-c", commandAndArgsStmt)
                .directory(workingDir.toFile())
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)

            val environment = procBuilder.environment()
            println(environment)

            val proc = procBuilder.start()
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
