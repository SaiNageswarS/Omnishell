package com.kotlang.plugins

import com.kotlang.plugins.autoComplete.CommandAutoComplete
import com.kotlang.plugins.autoComplete.GitAutoComplete
import com.kotlang.plugins.autoComplete.PathAutoComplete
import java.nio.file.Path

abstract class AutoCompletePlugin(protected val maxSuggestions: Int) {
    abstract fun getAutoComplete(workingDir: Path, command: String): List<String>

    abstract fun isApplicable(command: String): Boolean

    companion object {
        fun autoComplete(workingDir: Path, command: String, maxSuggestions: Int = 2): List<String> {
            if (command.trim().isEmpty()) {
                return listOf()
            }

            val plugins = listOf(GitAutoComplete(maxSuggestions), PathAutoComplete(maxSuggestions),
                CommandAutoComplete(maxSuggestions))

            for (plugin in plugins) {
                if (plugin.isApplicable(command)) {
                    return plugin.getAutoComplete(workingDir, command)
                }
            }

            return listOf()
        }
    }
}