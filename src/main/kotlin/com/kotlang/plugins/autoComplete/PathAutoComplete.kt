package com.kotlang.plugins.autoComplete

import com.kotlang.plugins.AutoCompletePlugin
import com.kotlang.util.getCommandAndArguments
import com.kotlang.util.normalize
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

class PathAutoComplete: AutoCompletePlugin() {
    internal fun getPrefixPath(path: String): String {
        val folders = path.split("/")
        return folders.subList(0, folders.size - 1).joinToString(separator = "/")
    }

    internal fun getSearchObject(path: String): String {
        val folders = path.split("/")
        return folders[folders.size-1]
    }

    internal fun getFileNameAutoCompletion(workingDir: Path, lastArgument: String): List<String> {
        val prefixPath = Paths.get(getPrefixPath(lastArgument))
        val searchPath = prefixPath.normalize(workingDir)
        val searchTerm = getSearchObject(lastArgument)

        if (Files.notExists(searchPath)) {
            return listOf()
        }

        val searchList = Files.list(searchPath)
            .map { it.fileName.toString() }
            .filter { it.startsWith(searchTerm, ignoreCase = true) }
            .collect(Collectors.toList())

        val prefixPathString = if("$prefixPath".length > 0) "$prefixPath/" else ""
        return searchList.map { prefixPathString + it }
    }

    //assumes last argument as path and auto-completes last argument
    override fun getAutoComplete(workingDir: Path, command: String): List<String> {
        val commandAndArguments = command.getCommandAndArguments()
        val lastArgument = commandAndArguments[commandAndArguments.size-1]
        val completeFileNames = getFileNameAutoCompletion(workingDir, lastArgument)
        if (completeFileNames.isNotEmpty()) {
            val restOfTheCommand = commandAndArguments.subList(0, commandAndArguments.size-1)
                .joinToString(" ")
            return completeFileNames.map { "$restOfTheCommand $it" }
        }

        return listOf(command)
    }

    override fun isApplicable(command: String): Boolean {
        //default auto-complete is path auto-complete.
        //should be placed at the end of plugins.
        return true
    }
}