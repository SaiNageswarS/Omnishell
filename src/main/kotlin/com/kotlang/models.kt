package com.kotlang

import java.nio.file.Path
import java.util.*

enum class CommandState {
    SUCCESS, FAILED, RUNNING
}

data class CommandOutput(
    var output: String = "",
    var error: String = "",
    var state: CommandState = CommandState.RUNNING
)

data class HistoryItem(
    val command: String,
    val output: CommandOutput
)

data class ShellState(
    var currentWorkingDir: Path = Path.of(System.getProperty("user.home")),
    var historyItems: List<HistoryItem> = listOf(),
    var index: Int = 0,
    val id: String = UUID.randomUUID().toString()
)