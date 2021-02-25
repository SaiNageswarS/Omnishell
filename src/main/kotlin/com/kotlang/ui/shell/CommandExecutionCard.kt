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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kotlang.CommandState
import com.kotlang.formatters.ErrorText
import com.kotlang.formatters.Node
import com.kotlang.formatters.PlainText
import com.kotlang.ui.PromptIcon
import com.kotlang.util.Ticker
import com.kotlang.util.sanitize
import java.util.*

class CommandExecutionCard(val command: String) {
    private val state = mutableStateOf(CommandState.RUNNING)
    val refreshState: (CommandState) -> Unit = {
        //if execution is finished, stop the ticker
        if (it == CommandState.FAILED || it == CommandState.SUCCESS) {
            ticker!!.stop()
        }
        state.value = it
    }

    private val document = Collections.synchronizedList(
        mutableListOf<Node>())
    val appendOutput: (Node) -> Unit = { document.add(it) }

    //polling document for output
    private var ticker: Ticker? = null

    @Composable
    private fun CommandStateIcon(state: CommandState) {
        when(state) {
            CommandState.RUNNING ->
                Icon(
                    Icons.Default.Refresh, contentDescription = "",
                    modifier = Modifier.size(25.dp))
            CommandState.SUCCESS ->
                Icon(
                    Icons.Default.CheckCircle, contentDescription = "",
                    modifier = Modifier.size(25.dp), tint = Color.Green)
            CommandState.FAILED ->
                Icon(
                    Icons.Default.Warning, contentDescription = "",
                    modifier = Modifier.size(25.dp), tint = Color.Red)
        }
    }

    @Composable
    private fun OutputDisplay(tick: Int) {
        synchronized(this) {
            Column {
                for (child in document) {
                    when(child) {
                        is PlainText -> Text(child.literal.sanitize(), color = Color.DarkGray)
                        is ErrorText -> Text(child.literal.sanitize(), color = Color.Red)
                    }
                }
                Text(tick.toString(), modifier = Modifier.padding(0.dp, 10.dp))
            }
        }
    }

    @Composable
    fun Draw(shellStateVersion: Int) {
        val currentTick = mutableStateOf(1)
        if (ticker == null) {
            ticker = Ticker {
                currentTick.value = it
            }
            ticker!!.poll()
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
                    CommandStateIcon(state.value)
                    Text(shellStateVersion.toString())
                }

                OutputDisplay(currentTick.value)
            }
        }
    }
}