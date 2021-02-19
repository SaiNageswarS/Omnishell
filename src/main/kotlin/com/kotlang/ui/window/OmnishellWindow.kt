package com.kotlang.ui.window

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.ScrollableTabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kotlang.ui.WindowTitle
import com.kotlang.ui.shell.Shell
import com.kotlang.ui.tabs.TabHeader
import java.nio.file.Path

lateinit var changePathUiCb: (Path) -> Unit
private lateinit var refreshShellTabUICb: (Int) -> Unit
private var shellRefreshCount = 0

fun refreshShell() {
    refreshShellTabUICb(++shellRefreshCount)
}

class OmnishellWindow(
    private val shells: MutableList<Shell> = mutableListOf(Shell(index = 0), Shell(index = 1)),
) {
    private lateinit var changeTabUICb: (Int) -> Unit

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

    @Composable
    fun Draw() {
        val selectedTab = remember { mutableStateOf(0) }
        val currentTabWorkingDir = remember { mutableStateOf(shells[selectedTab.value].currentWorkingDir) }
        val shellStateVersion = remember { mutableStateOf(0) }
        refreshShellTabUICb = { refreshCnt: Int ->
            //update ui only if state changed in active tab.
            shellStateVersion.value = refreshCnt
        }

        changeTabUICb = { tabIndex: Int ->
            selectedTab.value = tabIndex
            currentTabWorkingDir.value = shells[selectedTab.value].currentWorkingDir
        }
        changePathUiCb = { newPath: Path ->
            shells[selectedTab.value].currentWorkingDir = newPath
            currentTabWorkingDir.value = newPath
        }

        val tabHeader = TabHeader(this)
        Column {
            WindowTitle()
            ScrollableTabRow(
                selectedTabIndex = selectedTab.value,
                tabs = {
                    //add current tabs
                    for (i in shells.indices) {
                        val windowTitle = shells[i].currentWorkingDir
                            .fileName.toString()
                        tabHeader.TabHeader(windowTitle, selectedTab.value == i, i)
                    }
                    //button to add new tab
                    tabHeader.AddNewTabButton()
                }
            )

            Row(modifier = Modifier.background(Color(red = 34, green = 51, blue = 68))) {
                FileTree().FileTreeWidget(currentTabWorkingDir.value)
                shells[selectedTab.value].Draw(shellStateVersion.value)
            }
        }
    }
}
