package com.kotlang.plugins.autoComplete

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Path

class CommandAutoCompleteTest {
    //project workspace
    private val workingDir = Path.of(File("").absolutePath)

    @Test
    fun testGetAutoComplete() {
        val testCommand = "gre"
        val completions = CommandAutoComplete(2).getAutoComplete(workingDir, testCommand)
        Assertions.assertTrue(completions.contains("grep"))
    }
}