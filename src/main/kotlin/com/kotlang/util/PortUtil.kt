package com.kotlang.util

object PortUtil {
    private const val startPort = 50051
    private var currentIdx = -1

    fun getFreePort(): Int {
        currentIdx += 1
        return startPort + currentIdx
    }
}