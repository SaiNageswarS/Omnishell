package com.kotlang.plugins

import com.kotlang.plugins.autoComplete.GitAutoComplete
import com.kotlang.plugins.autoComplete.PathAutoComplete
import java.nio.file.Path

abstract class AutoCompletePlugin {
    abstract fun getAutoComplete(workingDir: Path, command: String,
        invocationCount: Int): String

    abstract fun isApplicable(command: String): Boolean

    companion object {
        private val plugins = listOf(PathAutoComplete(),
            GitAutoComplete())

        fun autoComplete(workingDir: Path, command: String,
                         invocationCount: Int): String {
            for (plugin in plugins) {
                if (plugin.isApplicable(command)) {
                    return plugin.getAutoComplete(workingDir, command,
                        invocationCount)
                }
            }

            return command
        }
    }
}