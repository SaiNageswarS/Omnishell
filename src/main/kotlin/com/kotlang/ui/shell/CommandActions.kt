package com.kotlang.ui.shell

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kotlang.omnishell.CommandOutput

data class ActionDetail (
    val name: String,
    val handler: () -> Unit
)

data class CommandActions (
    val question: String,
    val actions: List<ActionDetail>
)

@Composable
fun CommandActionsPrompt(actions: CommandActions, clearHandler: () -> Unit) {
    Column {
        Divider(
            color = Color.LightGray,
            modifier = Modifier.padding(horizontal = 0.dp, vertical = 15.dp)
        )

        Text(actions.question, color = Color.Gray)
        Row (
            modifier = Modifier.padding(horizontal = 0.dp, vertical = 10.dp)
        ) {
            for (action in actions.actions) {
                OutlinedButton(
                    onClick = {
                        action.handler()
                        clearHandler()
                    },
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 0.dp)
                ) {
                    Text(action.name)
                }
            }

            OutlinedButton(
                onClick = clearHandler,
            ) {
                Text("Cancel")
            }
        }
    }
}

@Composable
fun CommandStateIcon(state: CommandOutput.Status) {
    when(state) {
        CommandOutput.Status.SUCCESS ->
            Icon(
                Icons.Default.CheckCircle, contentDescription = "",
                modifier = Modifier.size(25.dp), tint = Color.Green)
        CommandOutput.Status.FAILED ->
            Icon(
                Icons.Default.Warning, contentDescription = "",
                modifier = Modifier.size(25.dp), tint = Color.Red)
        else ->
            CircularProgressIndicator(modifier = Modifier.size(25.dp))
    }
}