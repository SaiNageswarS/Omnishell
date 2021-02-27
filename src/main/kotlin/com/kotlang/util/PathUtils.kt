package com.kotlang.util

import java.nio.file.FileSystems
import java.nio.file.Path

fun Path.normalize(workingDir: Path): Path {
    var destPath: Path = this

    val destPathParts = "$destPath".split("/")
    if (destPathParts[0] == "~") {
        destPath = Path.of(
                System.getProperty("user.home"),
                destPathParts.subList(1, destPathParts.size).joinToString(separator = "/")
        )
    }

    return if (destPath.isAbsolute)
        destPath
    else
        FileSystems.getDefault()
            .getPath(workingDir.toString(), destPath.toString())
            .normalize().toAbsolutePath()
}