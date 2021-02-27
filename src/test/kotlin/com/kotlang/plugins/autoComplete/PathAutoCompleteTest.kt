package com.kotlang.plugins.autoComplete

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Path

class PathAutoCompleteTest {
    //project workspace
    private val workingDir = Path.of(File("").absolutePath)

    @Test
    fun testGetPrefixPath() {
        val testPath = "/home/sainageswar/work"
        val prefixPath = PathAutoComplete().getPrefixPath(testPath)
        Assertions.assertEquals("/home/sainageswar", prefixPath)
    }

    @Test
    fun testGetSearchObject() {
        val testPath = "/home/sainageswar/work"
        val searchObj = PathAutoComplete().getSearchObject(testPath)
        Assertions.assertEquals(searchObj, "work")
    }

    @Test
    fun testGetFileNameAutoCompletion() {
        //searching for resources folder
        val searchFile = "src/main/res"
        val completions = PathAutoComplete().getFileNameAutoCompletion(
            workingDir, searchFile
        )
        Assertions.assertEquals(completions[0], "src/main/resources")
    }

    @Test
    fun testGetFileNameAutoCompletionNonExistentDir() {
        //searching for resources folder
        val searchFile = "src/main/notExisting/res"
        val completions = PathAutoComplete().getFileNameAutoCompletion(
            workingDir, searchFile
        )
        //no auto-completion
        Assertions.assertEquals(completions.size, 0)
    }
}