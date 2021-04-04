package com.kotlang.shellCommands

import com.kotlang.omnishell.CommandContext
import com.kotlang.omnishell.CommandOutput
import com.kotlang.omnishell.EnvVariableRequest
import com.kotlang.ui.shell.ActionDetail
import com.kotlang.ui.shell.CommandActions
import com.kotlang.ui.shell.CommandExecutionCard
import com.kotlang.ui.shell.Shell
import kotlinx.coroutines.runBlocking

class ExportCommand: ShellCommand() {
    override fun isApplicable(command: String): Boolean =
        command.startsWith("export") || command.startsWith("set")

    override fun execute(cmdInput: CommandContext, shell: Shell): CommandExecutionCard {
        val parts = cmdInput.command.split(" ")
        val exportArg = parts.subList(1, parts.size).joinToString(separator = " ")
        val (variable, value) = exportArg.split("=")
        val result = runBlocking { shell.hostAgent.environmentClient.exportEnvironmentVariable(
            EnvVariableRequest.newBuilder().setVariable(variable).setValueExpression(value).build()) }.value
        val cmdOutput = CommandOutput.newBuilder().setFormat(CommandOutput.TextFormat.PLAIN)
            .setText(result).build()

        val actions = CommandActions(question = "Do you want to save the variable permanently?",
            actions = listOf(
                ActionDetail("Yes") {
                    runBlocking { shell.hostAgent.environmentClient.saveEnvironmentVariable(
                        EnvVariableRequest.newBuilder().setVariable(variable).setValueExpression(result).build()
                    ) }
                }
            ))

        return CommandExecutionCard(cmdInput, shell, mutableListOf(cmdOutput), actions, CommandOutput.Status.SUCCESS)
    }
}