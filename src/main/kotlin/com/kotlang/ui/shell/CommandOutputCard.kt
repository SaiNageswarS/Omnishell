package com.kotlang.ui.shell

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kotlang.CommandOutput
import com.kotlang.CommandState
import com.kotlang.ui.PromptIcon
import com.kotlang.util.sanitize

class CommandOutputCard(val command: String,
                        val output: CommandOutput) {
    lateinit var refreshCommandOutput: () -> Unit

    @Composable
    private fun CommandStateIcon() {
        when(output.state) {
            CommandState.RUNNING ->
                Icon(
                    Icons.Default.Refresh, contentDescription = "",
                    modifier = Modifier.size(25.dp))
            CommandState.SUCCESS ->
                Icon(
                    Icons.Filled.CheckCircle, contentDescription = "",
                    modifier = Modifier.size(25.dp), tint = Color.Green)
            CommandState.FAILED ->
                Icon(
                    Icons.Filled.Warning, contentDescription = "",
                    modifier = Modifier.size(25.dp), tint = Color.Red)
        }
    }

    @Composable
    fun Draw(shellStateVersion: Int) {
        val commandOutput =  mutableStateOf(output.output)
        val commandError = mutableStateOf(output.error)

        refreshCommandOutput = {
            commandOutput.value = output.output
            commandError.value = output.error
        }

        Card(
            shape = RoundedCornerShape(8.dp),
            backgroundColor = Color.White,
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            //command
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Row(modifier = Modifier.fillMaxWidth(0.93f)) {
                        PromptIcon()
                        Text(command, color = Color.DarkGray)
                    }
                    CommandStateIcon()
                    Text(shellStateVersion.toString())
                }

                Text(commandOutput.value.sanitize(), color = Color.DarkGray)
                Text(commandError.value.sanitize(), color = Color.Red)
            }
        }
    }
}