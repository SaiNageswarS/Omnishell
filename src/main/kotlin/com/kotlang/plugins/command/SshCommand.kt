package com.kotlang.plugins.command

import com.kotlang.CommandOutput
import java.nio.file.Path
import com.jcraft.jsch.*

class SshCommand: CommandPlugin("ssh") {
    val jsch = JSch()

    override fun execute(workingDir: Path, commandAndArguments: List<String>): CommandOutput {
        val userHost = commandAndArguments[1].split("@")
        val host = userHost[1]
        val user = userHost[0].split(":")[0]
//        val password = user

        return CommandOutput(Path.of(""))
    }
}
