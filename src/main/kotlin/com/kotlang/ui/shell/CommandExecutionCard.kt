package com.kotlang.ui.shell

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlang.CommandState
import com.kotlang.formatters.Document
import com.kotlang.formatters.ErrorText
import com.kotlang.formatters.PlainText
import com.kotlang.ui.PromptIcon
import com.kotlang.util.Ticker
import com.kotlang.util.sanitize

class CommandExecutionCard(val command: String) {
    private val state = mutableStateOf(CommandState.RUNNING)
    val refreshState: (CommandState) -> Unit = {
        //if execution is finished, stop the ticker
        if (it == CommandState.FAILED || it == CommandState.SUCCESS) {
            ticker.stop()
        }
        state.value = it
    }

    val document = Document()
    //polling document for output
    private val ticker: Ticker = Ticker()

    //running process of this execution card
    var process: Process? = null

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
    private fun RunningProcessInput() {
        val processInput = remember { mutableStateOf("") }

        TextField(
            value = processInput.value,
            onValueChange = { newVal: String -> processInput.value = newVal },
            placeholder = { Text("Enter Value or CtrlLeft+C to kill process.") },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                disabledIndicatorColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White
            ),
            modifier = Modifier
                .shortcuts {
                    on(Key.CtrlLeft + Key.C) {
                        document.appendWord(ErrorText("^C (Interrupted)"))
                        process!!.destroy()
                    }
                }
                .onKeyEvent { keyEvent ->
                    when (keyEvent.key) {
                        Key.Enter -> {
                            val input = processInput.value+"\n\r"
                            //don't close the writer
                            process!!.outputStream?.
                            write(input.toByteArray())
                            process!!.outputStream?.flush()
                            document.appendWord(PlainText(input))
                            processInput.value = ""
                            true
                        }
                        else -> false
                    }
                }
        )
    }

    @Composable
    private fun OutputDisplay(tick: Int) {
        synchronized(this) {
            Column {
                for (child in document.lines) {
                    when(child) {
                        is PlainText -> Text(child.literal.sanitize(), color = Color.DarkGray)
                        is ErrorText -> Text(child.literal.sanitize(), color = Color.Red)
                    }
                }

                if (process != null && state.value == CommandState.RUNNING) {
                    RunningProcessInput()
                }
                if (tick > 0) {
                    Text(tick.toString(), modifier = Modifier.padding(0.dp, 10.dp),
                        color = Color.Gray)
                }
            }
        }
    }

    @Composable
    fun Draw(shellStateVersion: Int) {
        val currentTick = mutableStateOf(0)
        ticker.notify = {
            currentTick.value = it
        }
        ticker.poll()

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
                        Text(command, color = Color.DarkGray,
                            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                    CommandStateIcon(state.value)
                    Text(shellStateVersion.toString(), color = Color.Transparent)
                }

                OutputDisplay(currentTick.value)
            }
        }
    }
}