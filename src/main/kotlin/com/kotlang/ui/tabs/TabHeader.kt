package com.kotlang.ui.tabs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kotlang.ui.window.OmnishellWindow

class TabHeader(private val windowActions: OmnishellWindow) {
    @Composable
    fun TabHeader(title: String, selected: Boolean, index: Int) {
        Tab(
            selected = selected,
            onClick = { windowActions.changeTab(index) }
        ) {
            Row {
                Text(title, modifier = Modifier.padding(vertical = 10.dp))
                //close tab button
                IconButton(onClick = { windowActions.closeTab(index) }) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(horizontal=0.dp, vertical=5.dp))
                }
            }
        }
    }

    @Composable
    fun AddNewTabButton() {
        IconButton(
            onClick = { windowActions.addTab() },
        ) {
            Icon(
                Icons.Default.Add, contentDescription = "", modifier = Modifier
                    .size(40.dp)
                    .padding(5.dp)
                    .background(MaterialTheme.colors.secondaryVariant)
            )
        }
    }
}
