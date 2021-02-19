package com.kotlang

enum class CommandState {
    SUCCESS, FAILED, RUNNING
}

data class CommandOutput(
    var output: String = "",
    var error: String = "",
    var state: CommandState = CommandState.RUNNING
)
