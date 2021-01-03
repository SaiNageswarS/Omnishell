package com.kotlang.util

import java.io.IOException
import java.nio.file.FileSystems
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

fun Path.changePath(destination: Path): Path =
    if (destination.isAbsolute)
        destination
    else
        FileSystems.getDefault()
            .getPath(toString(), destination.toString())
            .normalize().toAbsolutePath()


fun String.runCommand(workingDir: Path, changePath: (Path) -> Unit): String? {
    try {
        val parts = this.split("\\s".toRegex())
        var newPath = workingDir
        var output = ""

        if (parts[0] == "cd") {
            newPath = workingDir.changePath(Paths.get(parts[1]))
        } else {
            val proc = ProcessBuilder(*parts.toTypedArray())
                .directory(workingDir.toFile())
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

            proc.waitFor(60, TimeUnit.MINUTES)
            output = proc.inputStream.bufferedReader().readText()
        }

        changePath(newPath)
        return output
    } catch(e: IOException) {
        e.printStackTrace()
        return null
    }
}
