package com.kotlang.actions

import com.kotlang.ShellState

class WindowActions(val shellStates: MutableList<ShellState>,
                    private val changeTabUICb: (Int) -> Unit) {
    fun changeTab(newTabIndex: Int) {
        changeTabUICb(newTabIndex)
    }

    fun closeTab(tabIndex: Int) {
        shellStates.removeAt(tabIndex)
        //re-assign tab indices
        for (i in shellStates.indices) {
            shellStates[i].index = i
        }
        val newTabIndex = 0
        changeTabUICb(newTabIndex)
    }

    fun addTab() {
        val newTab = ShellState(index = shellStates.size)
        shellStates.add(newTab)
        changeTabUICb(newTab.index)
    }
}