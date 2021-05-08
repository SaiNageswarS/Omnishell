package com.kotlang.ui.shell.executionCard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlang.omnishell.CommandContext
import com.kotlang.omnishell.CommandId
import com.kotlang.omnishell.CommandInputById
import com.kotlang.omnishell.CommandOutput
import com.kotlang.ui.PromptIcon
import com.kotlang.ui.shell.CommandActions
import com.kotlang.ui.shell.CommandActionsPrompt
import com.kotlang.ui.shell.CommandStateIcon
import com.kotlang.ui.shell.Shell
import com.kotlang.util.Ticker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.awt.event.KeyEvent

class CommandExecutionCard(
    private val cmd: CommandContext,
    private val shell: Shell,
    private var document: MutableList<CommandOutput> = mutableListOf(),
    private var actions: CommandActions? = null,
    initialStatus: CommandOutput.Status = CommandOutput.Status.INIT,
    ) {
    private val state= mutableStateOf(initialStatus)
    private lateinit var commandId: String
    //immutable - initialized at the time of execution
    private val osShell = shell.osShell.value

    //polling document for output
    private val ticker: Ticker = Ticker()

    @Composable
    private fun RunningProcessInput() {
        val scope = rememberCoroutineScope()
        val processInput = remember { mutableStateOf("") }
        val isTextMasked = remember { mutableStateOf(cmd.command.startsWith("sudo")) }

        TextField(
            value = processInput.value,
            onValueChange = { newVal: String -> processInput.value = newVal },
            placeholder = { Text("Enter Input to process or Ctrl+W to kill process.") },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                disabledIndicatorColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White
            ),
            visualTransformation = if (isTextMasked.value) PasswordVisualTransformation()
                else VisualTransformation.None,
            trailingIcon = {
                           IconButton(onClick = {
                               isTextMasked.value = !isTextMasked.value
                           }) {
                               Icon(Icons.Default.Lock, contentDescription = "",
                                tint = if (isTextMasked.value) Color.Red else Color.LightGray)
                           }
            },
            modifier = Modifier
                .padding(0.dp)
                .shortcuts {
                    on(Key.CtrlLeft + Key.W) {
                        document.add(CommandOutput.newBuilder().setText("^W (Interrupted)")
                            .setFormat(CommandOutput.TextFormat.ERROR).build())
                        runBlocking { shell.hostAgent.commandExecutionClient.killProcess(
                            CommandId.newBuilder().setId(commandId).setShell(osShell).build()
                        ) }
                    }
                }
                .onPreviewKeyEvent { keyEvent ->
                    when (keyEvent.key) {
                        Key.Enter -> {
                            if (keyEvent.nativeKeyEvent.id == KeyEvent.KEY_RELEASED)
                                return@onPreviewKeyEvent true

                            val input = processInput.value+"\n\r"
                            //don't close the writer
                            scope.launch {  shell.hostAgent.commandExecutionClient.giveInput(
                                CommandInputById.newBuilder().setShell(osShell).setCmdId(commandId).setInput(input).build()
                            ) }

                            val displayText = if (!isTextMasked.value) input
                                else "\n\r"

                            document.add(CommandOutput.newBuilder().setText(displayText)
                                .setFormat(CommandOutput.TextFormat.PLAIN)
                                .build())
                            processInput.value = ""
                            true
                        }
                        else -> false
                    }
                }
        )
    }

    private suspend fun pollExecutionClient() {
        if (state.value == CommandOutput.Status.INIT) {
            commandId = shell.hostAgent.commandExecutionClient.initiateCommand(cmd).id

            shell.hostAgent.commandExecutionClient.runCommand(
                CommandId.newBuilder().setId(commandId).setShell(osShell).build()
            ).collect {
                if (document.isEmpty() || document.last().format != it.format) {
                    document.add(it)
                } else {
                    val out = CommandOutput.newBuilder().setText(document.last().text + it.text)
                        .setFormat(it.format).build()
                    document[document.size-1] = out
                }

                state.value = it.status
            }
        }
    }

    private val executionOutputDisplay: ExecutionOutputDisplay = ExecutionOutputDisplay()

    @Composable
    fun Draw(shellStateVersion: Int) {
        val scope = rememberCoroutineScope()

        val currentTick = mutableStateOf(0)
        ticker.notify = {
            currentTick.value = it
        }
        if (state.value == CommandOutput.Status.FAILED || state.value == CommandOutput.Status.SUCCESS) {
            ticker.stop()
        } else {
            ticker.poll()
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
                        PromptIcon(osShell)
                        Text(cmd.command, color = Color.DarkGray,
                             fontWeight = FontWeight.Bold,
                             fontFamily = FontFamily.Monospace,
                             fontSize = 15.sp
                        )
                    }
                    CommandStateIcon(state.value)
                    Text(shellStateVersion.toString(), color = Color.Transparent)
                }

                executionOutputDisplay.Draw(currentTick.value, document)
                if (state.value == CommandOutput.Status.RUNNING) {
                    RunningProcessInput()
                }

                if (actions != null) {
                    CommandActionsPrompt(actions!!) {
                        actions = null
                        currentTick.value = currentTick.value + 1
                    }
                }
            }
        }

        scope.launch { pollExecutionClient() }
    }
}