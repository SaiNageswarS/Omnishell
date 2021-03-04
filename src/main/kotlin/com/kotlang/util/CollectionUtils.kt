package com.kotlang.util

import java.util.LinkedList

fun<T> List<T>.cloneAndAppend(item: T, limit: Int): List<T> {
    val newList = mutableListOf<T>()
    newList.addAll(this.takeLast(limit))
    newList.add(0, item)
    return newList
}

fun<T> MutableList<T>.cloneAndReplace(item: T, index: Int): MutableList<T> {
    val newList = mutableListOf<T>()
    newList.addAll(this)
    newList[index] = item
    return newList
}

fun String.sanitize(): String {
    return this.trim()
}

fun<T> List<T>.toLinkedList(removeDuplicates: Boolean = false) =
    if (removeDuplicates) LinkedList(this.distinct())
    else LinkedList(this)