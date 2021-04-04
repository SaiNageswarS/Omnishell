package com.kotlang.ui.shell

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kotlang.remoting.Wsl
import com.kotlang.ui.Chip
import com.kotlang.ui.dialogs.EnvironmentDialog

class ShellHeader {
    @Composable
    fun currentHostInfo(host: String, currentWorkingDir: String) {
        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
            Card(
                shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp),
                backgroundColor =  Color(red = 253, green = 224, blue = 222),
            ) {
                Text(
                    host,
                    color = Color.DarkGray,
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
                    modifier = Modifier.padding(10.dp)
                )
            }

            Card(
                backgroundColor =  Color(red = 254, green = 252, blue = 218),
                shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp),
                modifier = Modifier.fillMaxWidth(0.8f).clickable { EnvironmentDialog() }
            ) {
                Text(
                    "Env",
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }

    @Composable
    fun remoteOptions() {
        val wslRemotes = Wsl.listWsl()

        Row(
            modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth().
            fillMaxHeight(),
        ) {
            Chip("WSL", wslRemotes) {
                println("Selected $it")
            }
            Spacer(Modifier.width(30.dp))

            Chip("SSH") {}
            Spacer(Modifier.width(30.dp))

            Chip("Docker") {}
        }
    }

    @Composable
    fun Draw(host: String, currentWorkingDir: String) {
        TopAppBar(
            modifier = Modifier.height(105.dp)
        ) {
            Column {
                currentHostInfo(host, currentWorkingDir)
                remoteOptions()
            }
        }
    }
}