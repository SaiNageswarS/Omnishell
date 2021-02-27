package com.kotlang.plugins.command

import java.nio.file.Path
import com.jcraft.jsch.*
import com.kotlang.CommandState
import com.kotlang.formatters.ErrorText
import com.kotlang.plugins.CommandPlugin
import com.kotlang.ui.shell.CommandExecutionCard
import com.kotlang.ui.shell.Shell

class SshCommand: CommandPlugin() {
    val jsch = JSch()

    override fun execute(workingDir: Path, commandAndArgsStmt: String,
                         shellActions: Shell, executionCard: CommandExecutionCard) {
//        val userHost = commandAndArgsStmt[1].split("@")
//        val host = userHost[1]
//        val user = userHost[0].split(":")[0]
//        val password = user
        executionCard.document.appendWord(ErrorText("SSH support is coming soon."))
        executionCard.refreshState(CommandState.FAILED)
    }

    override fun isApplicable(command: String): Boolean = command.startsWith("ssh")
}
