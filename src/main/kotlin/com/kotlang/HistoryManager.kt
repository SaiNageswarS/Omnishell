package com.kotlang

import com.kotlang.util.normalize
import com.kotlang.util.toLinkedList
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.*

val historyManager = HistoryManager(Path.of("~/.bash_history").normalize(
    Path.of("/")
))

class HistoryManager(private val bashHistoryFilePath: Path) {
    val history: LinkedList<String> = Files.readAllLines(bashHistoryFilePath)
        .reversed().toLinkedList(true)
    private val historySet = history.toMutableSet()

    fun addToHistory(command: String) {
        if (!historySet.contains(command)) {
            history.addFirst(command)
            historySet.add(command)
            Files.writeString(bashHistoryFilePath, command+"\n", StandardOpenOption.APPEND)
        }
    }

    fun searchHistory(prefix: String, limit: Int = 5): List<String> =
        if (prefix.length > 2) history.filter { it.startsWith(prefix) }.take(limit).toSet().toList()
        else listOf()
}