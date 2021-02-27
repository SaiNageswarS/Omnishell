package com.kotlang.plugins.autoComplete

import com.kotlang.plugins.AutoCompletePlugin
import com.kotlang.util.getCommandAndArguments
import java.nio.file.Path

class GitAutoComplete: AutoCompletePlugin() {
    val searchList = listOf("status", "clone", "init", "add", "mv", "restore", "rm",
        "sparse-checkout", "bisect", "diff", "grep", "log", "show",
        "branch", "commit", "merge", "rebase", "reset", "switch", "tag", "fetch",
        "pull", "push")

    override fun getAutoComplete(workingDir: Path, command: String): List<String> {
        val commandAndArguments = command.getCommandAndArguments()

        if (commandAndArguments.size == 2) {
            val searchTerm = commandAndArguments[1]

            val possibleCompletion = searchList
                .filter { it.startsWith(searchTerm, ignoreCase = true) }

            return possibleCompletion.map {
                commandAndArguments[0] + " " + it
            }
        }

        return listOf(command)
    }

    override fun isApplicable(command: String): Boolean {
        val commandAndArguments = command.getCommandAndArguments()
        return commandAndArguments[0] == "git"
    }
}