package com.kotlang.util

fun String.getCommandAndArguments(): List<String> =
    this.split("\\s(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*\$)".toRegex())