package com.kotlang.ui.shell.executionCard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlang.omnishell.CommandContext
import com.kotlang.omnishell.CommandId
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

class CommandExecutionCard(
        private val cmd: CommandContext,
        private val shell: Shell,
        private var document: MutableList<CommandOutput> = mutableListOf(),
        private var actions: CommandActions? = null,
        initialStatus: CommandOutput.Status = CommandOutput.Status.INIT,
    ) {
    private val state= mutableStateOf(initialStatus)
    //immutable - initialized at the time of execution
    private val osShell = shell.osShell.value

    // poll hostAgent execution client. Has to be an async background running process.
    // should be called only once.
    private suspend fun pollExecutionClient(commandId: String) {
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

    private val executionOutputDisplay: ExecutionOutputDisplay = ExecutionOutputDisplay()
    private val executionInput: ExecutionInput = ExecutionInput(
        shell.hostAgent,
        osShell,
        document,
        cmd.command.startsWith("sudo")
    )

    private var outputPollingTicker: Ticker? = null
    private fun getOutputPollingTicker(notify: (Int) -> Unit): Ticker {
        if (outputPollingTicker == null) {
            outputPollingTicker = Ticker(notify)
        }
        return outputPollingTicker!!
    }

    @Composable
    fun Draw(shellStateVersion: Int) {
        val scope = rememberCoroutineScope()
        val currentTick = mutableStateOf(0)

        var commandId: String? = null
        if (state.value == CommandOutput.Status.INIT) {
            commandId = runBlocking {
                shell.hostAgent.commandExecutionClient.initiateCommand(cmd)
            }.id
            state.value = CommandOutput.Status.RUNNING
            scope.launch { pollExecutionClient(commandId) }
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
                if (state.value == CommandOutput.Status.RUNNING && commandId != null) {
                    executionInput.Draw(commandId)
                }

                if (actions != null) {
                    CommandActionsPrompt(actions!!) {
                        actions = null
                        currentTick.value = currentTick.value + 1
                    }
                }
            }
        }

        val ticker = getOutputPollingTicker {
            currentTick.value = it
        }

        if (state.value == CommandOutput.Status.FAILED || state.value == CommandOutput.Status.SUCCESS) {
            ticker.stop()
        } else {
            ticker.poll()
        }
    }
}