package com.kotlang.formatters

import java.util.*

open class Node()

open class TextNode(var literal: String): Node() {
    init {
        literal = literal.replace("\t", "    ")
    }
}
class ErrorText(literal: String): TextNode(literal)
class PlainText(literal: String): TextNode(literal)

class Document {
    val lines: MutableList<TextNode> = Collections.synchronizedList(
        mutableListOf<TextNode>())
    var newLine: Boolean = true

    fun appendWord(node: TextNode) {
        synchronized(this) {
            if (newLine) {
                lines.add(node)
                newLine = false
                return
            }

            val lastNode = lines[lines.size - 1]
            lastNode.literal += node.literal + " "
            if (node.literal.endsWith("\n") ||
                node.literal.endsWith("\r")) {
                newLine = true
            }
        }
    }
}