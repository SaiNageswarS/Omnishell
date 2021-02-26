package com.kotlang.plugins.autoComplete

import com.kotlang.plugins.AutoCompletePlugin
import com.kotlang.util.getCommandAndArguments
import com.kotlang.util.normalize
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

class PathAutoComplete: AutoCompletePlugin() {
    fun getPrefixPath(path: String): String {
        val folders = path.split("/")
        return folders.subList(0, folders.size - 1).joinToString(separator = "/")
    }

    fun getSearchObject(path: String): String {
        val folders = path.split("/")
        return folders[folders.size-1]
    }

    override fun getAutoComplete(workingDir: Path, command: String,
        invocationCount: Int): String {
        val commandAndArguments = command.getCommandAndArguments()

        if (commandAndArguments.size > 1) {
            val prefixPath = Paths.get(getPrefixPath(commandAndArguments[1]))
            val searchPath = prefixPath.normalize(workingDir)
            val searchTerm = getSearchObject(commandAndArguments[1])

            val searchList = Files.list(searchPath)
                .map { it.fileName.toString() }
                .filter { it.startsWith(searchTerm, ignoreCase = true) }
                .collect(Collectors.toList())

            return commandAndArguments[0] + " " +
                    (if("$prefixPath".length > 0) "$prefixPath/" else "") +
                    searchList[invocationCount % searchList.size]
        }

        return command
    }

    override fun isApplicable(command: String): Boolean {
        val commandAndArguments = command.getCommandAndArguments()
        return listOf("cd", "ls", "cat", "cp", "mv", "rm")
            .contains(commandAndArguments[0])
    }
}