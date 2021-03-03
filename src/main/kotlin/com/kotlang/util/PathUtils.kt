package com.kotlang.util

import java.nio.file.FileSystems
import java.nio.file.Path

fun Path.normalize(workingDir: Path): Path {
    val destPath = Path.of(
        "$this".split("/").joinToString(separator = "/") {
            if (it == "~") System.getProperty("user.home") else it }
    )

    return if (destPath.isAbsolute)
        destPath
    else
        FileSystems.getDefault()
            .getPath(workingDir.toString(), destPath.toString())
            .normalize().toAbsolutePath()
}