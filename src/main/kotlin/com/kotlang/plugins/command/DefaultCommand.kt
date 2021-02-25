package com.kotlang.plugins.command

import com.kotlang.CommandState
import com.kotlang.Environment
import com.kotlang.formatters.ErrorText
import com.kotlang.plugins.CommandPlugin
import com.kotlang.ui.shell.CommandExecutionCard
import com.kotlang.ui.shell.Shell
import com.kotlang.util.gobbleStream
import java.nio.file.Path

class DefaultCommand: CommandPlugin(".*") {
    override fun execute(workingDir: Path, commandAndArgsStmt: String,
                         shellActions: Shell, commandExecutionCard: CommandExecutionCard) {
        val finalState = try {
            val procBuilder = ProcessBuilder("/bin/sh", "-c", commandAndArgsStmt)
                .directory(workingDir.toFile())
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)

            val environment = procBuilder.environment()
            environment.putAll(Environment.getEnvironment())
            println(environment)

            val proc = procBuilder.start()

            val streamTasks = listOf(
                gobbleStream(proc.inputStream, commandExecutionCard.appendOutput, false),
                gobbleStream(proc.errorStream, commandExecutionCard.appendOutput, true))

            proc.waitFor()
            streamTasks.forEach { it.join() }

            when(proc.exitValue()) {
                0 -> CommandState.SUCCESS
                else -> CommandState.FAILED
            }
        } catch (e: Exception) {
            commandExecutionCard.appendOutput(ErrorText(e.message ?: "Failed"))
            e.printStackTrace()
            CommandState.FAILED
        }

        commandExecutionCard.refreshState(finalState)
    }
}
