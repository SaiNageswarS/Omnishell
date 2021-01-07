package com.kotlang.util

import com.kotlang.HistoryItem
import java.io.IOException
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

fun Path.changePath(destination: Path): Path {
    val newPath = if (destination.isAbsolute)
        destination
    else
        FileSystems.getDefault()
            .getPath(toString(), destination.toString())
            .normalize().toAbsolutePath()

    if (Files.notExists(newPath) || !Files.isDirectory(newPath)) {
        throw Exception("Path doesn't exist")
    }

    return newPath;
}


fun runCommand(workingDir: Path, command: String, refreshShellTab: (Path, HistoryItem) -> Unit) {
    val commandOutput = HistoryItem(command)
    var newPath = workingDir

    try {
        val parts = command.split("\\s(?=(?:[^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*\$)".toRegex())

        if (parts[0] == "cd") {
            newPath = workingDir.changePath(Paths.get(parts[1]))
        } else {
            val proc = ProcessBuilder(*parts.toTypedArray())
                .directory(workingDir.toFile())
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

            proc.waitFor(60, TimeUnit.MINUTES)
            commandOutput.output = proc.inputStream.bufferedReader().readText()
            commandOutput.error = proc.errorStream.bufferedReader().readText()
        }
    } catch(e: java.lang.Exception) {
        commandOutput.error = e.message
        e.printStackTrace()
    }
    refreshShellTab(newPath, commandOutput)
}
