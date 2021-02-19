package com.kotlang.plugins.command

import com.kotlang.CommandState
import com.kotlang.HistoryItem
import com.kotlang.plugins.CommandPlugin
import com.kotlang.actions.ShellActions
import com.kotlang.util.gobbleStream
import java.nio.file.Path

class DefaultCommand: CommandPlugin(".*") {
    override fun execute(workingDir: Path, commandAndArgsStmt: String,
                shellActions: ShellActions, historyItem: HistoryItem) {
        val commandOutput = historyItem.output

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
            commandOutput.state = when(proc.exitValue()) {
                0 -> CommandState.SUCCESS
                else -> CommandState.FAILED
            }
        } catch (e: Exception) {
            commandOutput.error = e.message ?: "Failed"
            e.printStackTrace()
        }
    }
}
