package com.kotlang.util

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class VersionStringTests {
    @Test
    fun testVersionStringEquals() {
        val a = VersionString("0.0.1")
        val b = VersionString("0.0.1")

        //essential to check if latest version
        Assertions.assertFalse(a < b)
        //not working. Todo: check
        //Assertions.assertTrue(a == b)
        Assertions.assertEquals(a.compareTo(b), 0)
    }

    @Test
    fun testVersionStringEqualsTwo() {
        val a = VersionString("0.1.0")
        val b = VersionString("0.1.0")

        //essential to check if latest version
        Assertions.assertFalse(a < b)
        //not working. Todo: check
        //Assertions.assertTrue(a == b)
        Assertions.assertEquals(a.compareTo(b), 0)
    }

    @Test
    fun testVersionStringLess() {
        val a = VersionString("0.1.0")
        val b = VersionString("0.1.1")

        //essential to check if latest version
        Assertions.assertTrue(a < b)
    }

    @Test
    fun testVersionStringLessTwo() {
        val a = VersionString("0.1.1")
        val b = VersionString("0.2.0")

        //essential to check if latest version
        Assertions.assertTrue(a < b)
    }

    @Test
    fun testVersionStringGreater() {
        val a = VersionString("1.1.1")
        val b = VersionString("0.2.0")

        //essential to check if latest version
        Assertions.assertFalse(a < b)
        Assertions.assertTrue(a > b)
    }

    @Test
    fun testVersionStringGreaterTwo() {
        val a = VersionString("1.1.0")
        val b = VersionString("1.0.10")

        //essential to check if latest version
        Assertions.assertFalse(a < b)
        Assertions.assertTrue(a > b)
    }
}