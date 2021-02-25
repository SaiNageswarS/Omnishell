package com.kotlang.util

import com.kotlang.formatters.ErrorText
import com.kotlang.formatters.Node
import com.kotlang.formatters.PlainText
import java.io.InputStream
import java.util.*

private fun getNode(line: String, isError: Boolean) =
    if (isError)
        ErrorText(line)
    else
        PlainText(line)

fun gobbleStream(source: InputStream, refreshCommandOutput: (Node) -> Unit,
                 isError: Boolean): Thread {
    val task = Thread {
        val sc = Scanner(source)
        while (sc.hasNextLine()) {
            val line = sc.nextLine()
            refreshCommandOutput(getNode(line, isError))
            println(line)
        }
    }
    task.start()
    return task
}