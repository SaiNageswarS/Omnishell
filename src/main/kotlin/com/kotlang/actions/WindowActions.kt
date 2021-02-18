package com.kotlang.actions

import com.kotlang.ShellState

class WindowActions(val shellStates: MutableList<ShellState>,
                    private val changeTabUICb: (Int) -> Unit) {
    var selectedTab: ShellState = shellStates[0]
        set(value) {
            field = value
            changeTabUICb(value.index)
        }

    fun changeTab(newTabIndex: Int) {
        selectedTab = shellStates[newTabIndex]
    }

    fun closeTab(tabIndex: Int) {
        shellStates.removeAt(tabIndex)
        //re-assign tab indices
        for (i in shellStates.indices) {
            shellStates[i].index = i
        }
        val newTabIndex = if (selectedTab.index == tabIndex) 0 else selectedTab.index
        selectedTab = shellStates[newTabIndex]
    }

    fun addTab() {
        val newTab = ShellState(index = shellStates.size)
        shellStates.add(newTab)
        selectedTab = newTab
    }
}