package com.kotlang.util

import com.kotlang.state.ActiveShellState
import java.io.InputStream
import java.util.*

fun gobbleStream(source: InputStream, output: StringBuffer): Thread {
    val task = Thread {
        val sc = Scanner(source)
        while (sc.hasNextLine()) {
            val line = sc.nextLine()
            output.append(line).append("\n")
            println(line)
        }
    }
    task.start()
    return task
}