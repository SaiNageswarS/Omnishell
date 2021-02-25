package com.kotlang.formatters

open class Node()

class ErrorText(val literal: String): Node()
class PlainText(val literal: String): Node()