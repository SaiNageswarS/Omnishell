package com.kotlang.util

import java.nio.file.FileSystems
import java.nio.file.Path

fun Path.normalize(workingDir: Path): Path =
    if (this.isAbsolute)
        this
    else
        FileSystems.getDefault()
            .getPath(workingDir.toString(), this.toString())
            .normalize().toAbsolutePath()