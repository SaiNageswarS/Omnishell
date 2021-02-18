package com.kotlang

import java.nio.file.Path

data class CommandOutput(
    var output: String? = null,
    var error: String? = null
)

data class HistoryItem(
    val command: String,
    val output: CommandOutput
)

data class ShellState(
    var currentWorkingDir: Path = Path.of(System.getProperty("user.home")),
    var historyItems: List<HistoryItem> = listOf(),
    var index: Int = 0
)