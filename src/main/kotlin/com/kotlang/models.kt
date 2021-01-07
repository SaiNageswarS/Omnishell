package com.kotlang

import java.nio.file.Path

data class CommandOutput(
    var path: Path,
    var output: String? = null,
    var error: String? = null
)

data class HistoryItem(
    val command: String,
    var output: CommandOutput
)

data class ShellTabData(
    var currentWorkingDir: Path = Path.of(System.getProperty("user.home")),
    var historyItems: List<HistoryItem> = listOf()
)
