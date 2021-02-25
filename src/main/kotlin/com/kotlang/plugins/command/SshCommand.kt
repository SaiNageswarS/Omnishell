package com.kotlang.plugins.command

import java.nio.file.Path
import com.jcraft.jsch.*
import com.kotlang.plugins.CommandPlugin
import com.kotlang.ui.shell.CommandExecutionCard
import com.kotlang.ui.shell.Shell

class SshCommand: CommandPlugin("ssh\\s.*") {
    val jsch = JSch()

    override fun execute(workingDir: Path, commandAndArgsStmt: String,
                         shellActions: Shell, commandExecutionCard: CommandExecutionCard) {
//        val userHost = commandAndArgsStmt[1].split("@")
//        val host = userHost[1]
//        val user = userHost[0].split(":")[0]
//        val password = user
    }
}
