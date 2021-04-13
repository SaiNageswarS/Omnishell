package com.kotlang.shellCommands

import com.kotlang.omnishell.CommandContext
import com.kotlang.omnishell.CommandOutput
import com.kotlang.omnishell.ReplRequest
import com.kotlang.ui.shell.CommandExecutionCard
import com.kotlang.ui.shell.Shell
import com.kotlang.util.PortUtil
import kotlinx.coroutines.runBlocking
import org.apache.logging.log4j.LogManager
import java.net.Socket

class ReplCommand: ShellCommand() {
    companion object {
        @JvmStatic
        private val logger = LogManager.getLogger(ReplCommand::class.java)
    }

    override fun isApplicable(command: String): Boolean {
        return command == "node"
    }

    override fun execute(cmdInput: CommandContext, shell: Shell): CommandExecutionCard {
        try {
            runBlocking { shell.hostAgent.commandExecutionClient.addRepl(
                ReplRequest.newBuilder().setLanguage(cmdInput.command)
                    .setPort(PortUtil.getFreePort().toString())
                    .build())
            }
            shell.osShell.value = "node"
            shell.availableOsShells.add("node")
            return CommandExecutionCard(cmdInput, shell, initialStatus = CommandOutput.Status.SUCCESS)
        } catch(e: Exception) {
            logger.error("Failed starting repl server", e)
            val error = CommandOutput.newBuilder().setText(e.message)
                .setFormat(CommandOutput.TextFormat.ERROR).build()
            return CommandExecutionCard(cmdInput, shell, mutableListOf(error),
                initialStatus = CommandOutput.Status.FAILED)
        }
    }
}