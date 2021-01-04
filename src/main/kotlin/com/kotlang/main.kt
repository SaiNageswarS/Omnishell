package com.kotlang

import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.kotlang.ui.tabs.ShellTab
import com.kotlang.ui.tabs.ShellTabRowItem

object WindowState {
    val shellStates = mutableListOf(ShellTabData(), ShellTabData())
}

fun main() = Window(title = "OmniShell") {
    val selectedTab = remember { mutableStateOf(0) }
    val changeTab = { tabIndex: Int -> selectedTab.value = tabIndex }

    MaterialTheme {
        Column {
            ScrollableTabRow(
                selectedTabIndex = selectedTab.value,
                tabs = {
                    ShellTabRowItem("home1", true, 0, changeTab)
                    ShellTabRowItem("home2", false, 1, changeTab)
                }
            )

            ShellTab(selectedTab.value)
        }
    }
}
