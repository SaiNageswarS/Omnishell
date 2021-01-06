package com.kotlang.ui.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AmbientContentColor
import androidx.compose.material.Icon
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShellTabRowItem(title: String, selected: Boolean,
                    index: Int, close: (Int) -> Unit, changeTab: (Int) -> Unit) {
    Tab(
        selected = selected,
        onClick = { changeTab(index) }
    ) {
        Row {
            Text(title, modifier = Modifier.padding(5.dp))
            Icon(
                Icons.Default.Close, tint = AmbientContentColor.current, modifier = Modifier
                    .size(24.dp)
                    .padding(5.dp)
                    .clickable {
                        close(index)
                    })
        }
    }
}

@Composable
fun AddTabItem(addTab: () -> Unit) {
    Tab(
        selected = false,
        onClick = addTab,
        modifier = Modifier.background(Color.LightGray).size(50.dp)
    ) {
        Icon(
            Icons.Default.Add, tint = AmbientContentColor.current, modifier = Modifier
                .size(30.dp)
                .padding(5.dp))
    }
}