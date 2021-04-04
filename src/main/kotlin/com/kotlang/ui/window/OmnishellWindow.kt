package com.kotlang.ui.window

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ScrollableTabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.kotlang.ui.shell.Shell
import com.kotlang.ui.tabs.TabHeader
import java.nio.file.Path

private lateinit var refreshShellTabUICb: (Int) -> Unit
private var shellRefreshCount = 0

fun refreshShell() {
    refreshShellTabUICb(++shellRefreshCount)
}

/**
 * There will be only one window :)
 */
object OmnishellWindow {
    private lateinit var changeTabUICb: (Int) -> Unit
    private val shells: MutableList<Shell> = mutableListOf(Shell(index = 0), Shell(index = 1))

    fun changeTab(newTabIndex: Int) {
        changeTabUICb(newTabIndex)
    }

    fun closeTab(tabIndex: Int) {
        shells.removeAt(tabIndex)
        //re-assign tab indices
        for (i in shells.indices) {
            shells[i].index = i
        }
        val newTabIndex = 0
        changeTabUICb(newTabIndex)
    }

    fun addTab() {
        val newTab = Shell(index = shells.size)
        shells.add(newTab)
        changeTabUICb(newTab.index)
    }

    fun destroy() {
        for (shell in shells) {
            shell.destroy()
        }
    }

    @Composable
    fun Draw() {
        val selectedTab = remember { mutableStateOf(0) }
        val shellStateVersion = remember { mutableStateOf(0) }
        refreshShellTabUICb = { refreshCnt: Int ->
            //update ui only if state changed in active tab.
            shellStateVersion.value = refreshCnt
        }

        changeTabUICb = { tabIndex: Int ->
            selectedTab.value = tabIndex
        }

        val tabHeader = TabHeader(this)
        Column {
            ScrollableTabRow(
                selectedTabIndex = selectedTab.value,
                tabs = {
                    //add current tabs
                    for (i in shells.indices) {
                        val windowTitle = Path.of(shells[i].getCurrentWorkingDir())
                            .fileName.toString()
                        tabHeader.TabHeader(windowTitle, selectedTab.value == i, i)
                    }
                    //button to add new tab
                    tabHeader.AddNewTabButton()
                }
            )

            shells[selectedTab.value].Draw(shellStateVersion.value)
        }
    }
}
