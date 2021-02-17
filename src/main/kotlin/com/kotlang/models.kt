package com.kotlang

data class CommandOutput(
    var output: String? = null,
    var error: String? = null
)

data class HistoryItem(
    val command: String,
    var output: CommandOutput
)
