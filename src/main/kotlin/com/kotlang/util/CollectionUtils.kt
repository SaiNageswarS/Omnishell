package com.kotlang.util

fun<T> List<T>.cloneAndAppend(item: T, limit: Int): List<T> {
    val newList = mutableListOf<T>()
    newList.addAll(this.takeLast(limit))
    newList.add(item)
    return newList
}