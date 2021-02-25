package com.kotlang.util

import com.kotlang.formatters.Document
import com.kotlang.formatters.ErrorText
import com.kotlang.formatters.PlainText
import java.io.InputStream
import java.util.*

private fun getNode(line: String, isError: Boolean) =
    if (isError)
        ErrorText(line)
    else
        PlainText(line)

fun gobbleStream(source: InputStream, document: Document,
                 isError: Boolean): Thread {
    val task = Thread {
        val sc = Scanner(source).useDelimiter("\\z")
        while (sc.hasNext()) {
            val line = sc.next()
            document.appendWord(getNode(line, isError))
            println(line)
        }
    }
    task.start()
    return task
}
