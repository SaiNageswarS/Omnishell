package com.kotlang.ui.tabs

import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun ShellTabRowItem(title: String, selected: Boolean,
                    index: Int, changeTab: (Int) -> Unit) {
    Tab(
        selected = selected,
        onClick = { changeTab(index) }
    ) {
        Text(title)
    }
}