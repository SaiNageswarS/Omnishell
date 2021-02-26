package com.kotlang.plugins.autoComplete

import com.kotlang.plugins.AutoCompletePlugin
import com.kotlang.util.getCommandAndArguments
import com.kotlang.util.normalize
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

class GitAutoComplete: AutoCompletePlugin() {
    val searchList = listOf("status", "clone", "init", "add", "mv", "restore", "rm",
        "sparse-checkout", "bisect", "diff", "grep", "log", "show",
        "branch", "commit", "merge", "rebase", "reset", "switch", "tag", "fetch",
        "pull", "push")

    override fun getAutoComplete(workingDir: Path, command: String, invocationCount: Int): String {
        val commandAndArguments = command.getCommandAndArguments()

        if (commandAndArguments.size == 2) {
            val searchTerm = commandAndArguments[1]

            val possibleCompletion = searchList
                .filter { it.startsWith(searchTerm, ignoreCase = true) }

            return commandAndArguments[0] + " " +
                    possibleCompletion[0]
        }

        return command
    }

    override fun isApplicable(command: String): Boolean {
        val commandAndArguments = command.getCommandAndArguments()
        return commandAndArguments[0] == "git"
    }
}