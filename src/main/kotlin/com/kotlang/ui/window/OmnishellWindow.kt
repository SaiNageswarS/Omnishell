package com.kotlang.ui.window

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ScrollableTabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import com.kotlang.remoting.LocalTargetManager
import com.kotlang.remoting.RemoteTargetManager
import com.kotlang.remoting.WslTargetManager
import com.kotlang.ui.shell.Shell
import com.kotlang.ui.tabs.TabHeader
import java.nio.file.Path

/**
 * There will be only one window :)
 */
object OmnishellWindow {
    private val shells: MutableList<Shell> = mutableListOf(Shell(index = 0, remoteTargetManager = LocalTargetManager()))

    private val selectedTab = mutableStateOf(0)

    fun changeTab(newTabIndex: Int) {
        selectedTab.value = newTabIndex
    }

    fun closeTab(tabIndex: Int) {
        shells[tabIndex].destroy()
        shells.removeAt(tabIndex)
        //re-assign tab indices
        for (i in shells.indices) {
            shells[i].index = i
        }
        val newTabIndex = 0
        selectedTab.value = newTabIndex
    }

    fun addTab(target: RemoteTargetManager = LocalTargetManager()) {
        val newTab = Shell(index = shells.size, remoteTargetManager = target)
        shells.add(newTab)
        selectedTab.value = newTab.index
    }

    fun destroy() {
        for (shell in shells) {
            shell.destroy()
        }
    }

    private val shellStateVersion = mutableStateOf(0)
    fun refreshShell() {
        shellStateVersion.value = shellStateVersion.value + 1
    }

    @Composable
    fun Draw() {
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
