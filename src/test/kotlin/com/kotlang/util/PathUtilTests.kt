package com.kotlang.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Path

class PathUtilTests {
    private val workingDir = Path.of(File("").absolutePath)

    @Test
    fun normalizePathShouldReturnSameForAbsolute() {
        val sampleAbsPath = Path.of("$workingDir/src")
        val destPath = sampleAbsPath.normalize(workingDir)

        println("Sample abs path $sampleAbsPath")
        println("Dest abs path $destPath")
        Assertions.assertEquals(sampleAbsPath, destPath)
    }

    @Test
    fun normalizePathShouldWorkForRelativePath() {
        val sampleRelPath = Path.of("src")
        val destPath = sampleRelPath.normalize(workingDir)

        println("Sample rel path $sampleRelPath")
        println("Dest abs path $destPath")
        Assertions.assertEquals("$workingDir/src", "$destPath")
    }
}