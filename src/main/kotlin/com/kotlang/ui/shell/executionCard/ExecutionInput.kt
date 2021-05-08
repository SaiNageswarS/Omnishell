package com.kotlang.ui.shell.executionCard

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.kotlang.HostAgent
import com.kotlang.omnishell.CommandId
import com.kotlang.omnishell.CommandInputById
import com.kotlang.omnishell.CommandOutput
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.awt.event.KeyEvent

class ExecutionInput(
    private val hostAgent: HostAgent,
    private val osShell: String,
    private val document: MutableList<CommandOutput>,
    isSudo: Boolean
) {
    private val isTextMasked = mutableStateOf(isSudo)

    private fun interrupt(commandId: String) {
        document.add(
            CommandOutput.newBuilder().setText("^W (Interrupted)")
                .setFormat(CommandOutput.TextFormat.ERROR).build())
        runBlocking { hostAgent.commandExecutionClient.killProcess(
            CommandId.newBuilder().setId(commandId).setShell(osShell).build()
        ) }
    }

    private fun handleEnter(scope: CoroutineScope, processInput: String, commandId: String) {
        val input = processInput + "\n\r"
        //don't close the writer
        scope.launch {  hostAgent.commandExecutionClient.giveInput(
            CommandInputById.newBuilder().setShell(osShell).setCmdId(commandId).setInput(input).build()
        ) }

        val displayText = if (!isTextMasked.value) input
        else "\n\r"

        document.add(
            CommandOutput.newBuilder().setText(displayText)
                .setFormat(CommandOutput.TextFormat.PLAIN)
                .build())
    }

    @Composable
    fun Draw(commandId: String) {
        val scope = rememberCoroutineScope()
        val processInput = remember { mutableStateOf("") }

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
                    Icon(
                        Icons.Default.Lock, contentDescription = "",
                        tint = if (isTextMasked.value) Color.Red else Color.LightGray)
                }
            },
            modifier = Modifier
                .padding(0.dp)
                .shortcuts {
                    on(Key.CtrlLeft + Key.W) {
                        interrupt(commandId)
                    }
                }
                .onPreviewKeyEvent { keyEvent ->
                    when (keyEvent.key) {
                        Key.Enter -> {
                            if (keyEvent.nativeKeyEvent.id == KeyEvent.KEY_RELEASED)
                                return@onPreviewKeyEvent true
                            handleEnter(scope, processInput.value, commandId)
                            processInput.value = ""
                            true
                        }
                        else -> false
                    }
                }
        )
    }
}