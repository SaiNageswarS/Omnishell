package com.kotlang.ui.shell

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kotlang.remoting.LocalTargetManager
import com.kotlang.remoting.WslTargetManager
import com.kotlang.ui.Chip
import com.kotlang.ui.dialogs.EnvironmentDialog
import com.kotlang.ui.window.OmnishellWindow

class ShellHeader(private val shell: Shell) {
    fun getHost(): String {
        return when(shell.remoteTargetManager) {
            is LocalTargetManager -> "localhost"
            is WslTargetManager -> "wsl"
            else -> "ssh"
        }
    }

    @Composable
    fun currentHostInfo(currentWorkingDir: String) {
        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
            Card(
                shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp),
                backgroundColor =  Color(red = 253, green = 224, blue = 222),
            ) {
                Text(
                    getHost(),
                    color = Color.DarkGray,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Card(
                backgroundColor =  Color.White,
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(
                    currentWorkingDir,
                    color = Color.DarkGray,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(10.dp)
                )
            }

            Card(
                backgroundColor =  Color(red = 254, green = 252, blue = 218),
                shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp),
                modifier = Modifier.fillMaxWidth(0.8f).clickable { EnvironmentDialog(shell) }
            ) {
                Text(
                    "Env",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }

    @Composable
    fun remoteOptions() {
        val wslRemotes = WslTargetManager.listWsl()

        Row(
            modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth().
            fillMaxHeight(),
        ) {
            Chip("WSL", wslRemotes) {
                OmnishellWindow.addTab(WslTargetManager(wslRemotes[it]))
            }
            Spacer(Modifier.width(30.dp))

            Chip("SSH") {}
            Spacer(Modifier.width(30.dp))

            Chip("Docker") {}
        }
    }

    @Composable
    fun Draw(currentWorkingDir: String) {
        TopAppBar(
            modifier = Modifier.height(105.dp)
        ) {
            Column {
                currentHostInfo(currentWorkingDir)
                remoteOptions()
            }
        }
    }
}