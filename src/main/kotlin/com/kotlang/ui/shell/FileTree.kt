package com.kotlang.ui.shell

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.vectorXmlResource
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlang.hostAgent
import com.kotlang.omnishell.CommandContext
import com.kotlang.omnishell.FileDetail
import kotlinx.coroutines.runBlocking

class FileTree(private val shell: Shell) {
    @Composable
    private fun FileIcon(fileDetail: FileDetail) {
        val fileType = when {
            fileDetail.isDirectory -> "folder"
            fileDetail.isExecutable -> "exe"
            else -> fileDetail.extension
        }

        val iconDisplay = when(fileType) {
            "folder" -> Pair(vectorXmlResource("images/folder_black_18dp.xml"),
                          MaterialTheme.colors.secondaryVariant)
            "jpg", "svg", "png" -> Pair(vectorXmlResource("images/image_black_18dp.xml"),
                          Color.White)
            "mp3", "wav" -> Pair(vectorXmlResource("images/music_note_black_18dp.xml"),
                          Color.White)
            "pdf" -> Pair(vectorXmlResource("images/picture_as_pdf_black_18dp.xml"),
                          Color.White)
            "exe" -> Pair(Icons.Default.PlayArrow, Color.Green)
            else -> Pair(vectorXmlResource("images/text_snippet_black_18dp.xml"), Color.White)
        }

        Icon(imageVector = iconDisplay.first,
            contentDescription = "",
            modifier = Modifier.width(18.dp),
            tint = iconDisplay.second
        )
    }

    @Composable
    private fun FileTreeItem(fileDetail: FileDetail) {
        var fileName = fileDetail.fileName.toString().take(18)

        if (fileDetail.fileName.toString().length > 18) {
            fileName = fileName.replaceRange(15, 17, ".")
        }

        Row( modifier = Modifier.padding(7.dp) ) {
            FileIcon(fileDetail)
            ClickableText(
                AnnotatedString(fileName),
                onClick = {
                    if (fileDetail.isDirectory) {
                        shell.changeDirectory(fileDetail.path)
                    }
                },
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 0.dp),
                style = TextStyle(color = Color.White)
            )
        }
    }

    @Composable
    fun FileTreeWidget(currentWorkingDir: String) {
        val fileList = runBlocking { hostAgent.fileSystemClient.getFileList(
            CommandContext.newBuilder().setWorkingDir(currentWorkingDir.toString()).build()).filesList }

        Column {
            Text(
                modifier = Modifier.padding(horizontal = 0.dp, vertical = 16.dp),
                text = "FOLDERS",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.White
            )

            LazyColumn(
                modifier = Modifier.width(220.dp).fillMaxHeight(),
            ) {
                itemsIndexed(fileList) { _, fileItem ->
                    FileTreeItem(fileItem)
                }
            }
        }
    }
}
