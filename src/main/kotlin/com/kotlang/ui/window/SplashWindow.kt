package com.kotlang.ui.window

import androidx.compose.desktop.AppWindow
import androidx.compose.foundation.background
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import com.kotlang.iconUrl
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import javax.imageio.ImageIO
import javax.swing.SwingUtilities
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class SplashWindow {
    private lateinit var splashWindow: AppWindow

    fun create() {
        SwingUtilities.invokeLater {
            splashWindow = AppWindow(
                resizable = false,
                icon = ImageIO.read(iconUrl),
                undecorated = true,
                size = IntSize(500, 250)
            )

            splashWindow.show {
                Column(
                    modifier = Modifier.background(color = Color(red = 0, green = 63, blue = 85))
                        .padding(15.dp)
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .fillMaxHeight(0.9f)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxHeight()
                                .fillMaxWidth(0.4f)
                        ) {
                            Image(
                                bitmap = imageResource("osIcon.png"),
                                contentDescription = "logo",
                                modifier = Modifier.size(width = 120.dp, height = 120.dp)
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxHeight()
                                .fillMaxWidth()
                        ) {
                            Text("Omnishell",
                                fontSize = 30.sp,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text("Modern shell empowering developer",
                                fontSize = 12.sp,
                                color = Color.White,
                            )
                        }
                    }
                    Text("Downloading Host Manager...", color = Color.White)
                }
            }
        }
    }

    fun close() {
        splashWindow.close()
    }
}