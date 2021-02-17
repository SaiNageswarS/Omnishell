package com.kotlang.state

import com.kotlang.ShellTabData

object WindowState {
    val shellStates = mutableListOf(ShellTabData(), ShellTabData())
    lateinit var changeTabUICb: (Int) -> Unit

    var selectedTabIndex: Int = 0
        set(value) {
            field = value
            changeTabUICb(value)
        }

    fun changeTab(newTabIndex: Int) {
        selectedTabIndex = newTabIndex
    }

    fun closeTab(tabIndex: Int) {
        shellStates.removeAt(tabIndex)
        val newTabIndex = if (selectedTabIndex == tabIndex) 0 else selectedTabIndex
        selectedTabIndex = newTabIndex
    }

    fun addTab() {
        shellStates.add(ShellTabData())
        selectedTabIndex = shellStates.size - 1
    }
}