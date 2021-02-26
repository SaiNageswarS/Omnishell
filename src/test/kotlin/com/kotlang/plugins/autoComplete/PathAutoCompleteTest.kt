package com.kotlang.plugins.autoComplete

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PathAutoCompleteTest {
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
}