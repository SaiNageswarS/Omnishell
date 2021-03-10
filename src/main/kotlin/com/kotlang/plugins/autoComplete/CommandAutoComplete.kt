package com.kotlang.plugins.autoComplete

import com.kotlang.Environment
import com.kotlang.plugins.AutoCompletePlugin
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

class CommandAutoComplete(maxSuggestions: Int): AutoCompletePlugin(maxSuggestions) {
    override fun getAutoComplete(workingDir: Path, command: String): List<String> {
        val cmdList = command.split(" ")
        val searchTerm = cmdList.last()
        val prefix = cmdList.subList(0, cmdList.size-1).joinToString(separator = " ")

        val binPaths = Environment.getEnvironment()["PATH"]?.split(":")
            ?: listOf()

        val searchList = mutableListOf<String>()
        for (binPath in binPaths) {
            val binaries =
                Files.list(Path.of(binPath)).map { it.fileName.toString() }
                    .filter { it.startsWith(searchTerm, ignoreCase = true) }
                    .limit(maxSuggestions.toLong())
                    .collect(Collectors.toList())
            searchList.addAll(binaries)

            if (searchList.size >= maxSuggestions) {
                break
            }
        }
        return searchList.map { prefix + it }
    }

    override fun isApplicable(command: String): Boolean {
        //default auto-complete
        return true
    }

}