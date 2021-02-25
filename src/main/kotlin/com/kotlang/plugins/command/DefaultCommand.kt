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
                         shellActions: Shell, executionCard: CommandExecutionCard) {
        val finalState = try {
            val procBuilder = ProcessBuilder("/bin/sh", "-c", commandAndArgsStmt)
                .directory(workingDir.toFile())
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .redirectInput(ProcessBuilder.Redirect.PIPE)

            val environment = procBuilder.environment()
            environment.putAll(Environment.getEnvironment())
            println(environment)

            executionCard.process = procBuilder.start()

            val streamTasks = listOf(
                gobbleStream(executionCard.process!!.inputStream, executionCard.document, false),
                gobbleStream(executionCard.process!!.errorStream, executionCard.document, true)
            )

            executionCard.process!!.waitFor()
            streamTasks.forEach { it.join() }

            when(executionCard.process!!.exitValue()) {
                0 -> CommandState.SUCCESS
                else -> CommandState.FAILED
            }
        } catch (e: Exception) {
            executionCard.document.appendWord(ErrorText(e.message ?: "Failed"))
            e.printStackTrace()
            CommandState.FAILED
        }

        executionCard.refreshState(finalState)
    }
}
