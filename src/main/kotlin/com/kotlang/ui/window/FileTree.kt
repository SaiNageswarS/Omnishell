package com.kotlang.ui.window

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.vectorXmlResource
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.apache.commons.io.FilenameUtils

class FileTree() {
    @Composable
    private fun FileIcon(fileDetail: Path) {
        val extension = if (Files.isDirectory(fileDetail)) "folder"
        else FilenameUtils.getExtension(fileDetail.toString())

        val iconPath = when(extension) {
            "folder" -> "images/folder_black_18dp.xml"
            "jpg", "svg", "png" -> "images/image_black_18dp.xml"
            "mp3", "wav" -> "music_note_black_18dp.xml"
            "pdf" -> "picture_as_pdf_black_18dp.xml"
            else -> "images/text_snippet_black_18dp.xml"
        }

        Icon(imageVector = vectorXmlResource(iconPath),
            contentDescription = "",
            modifier = Modifier.width(18.dp),
            tint = MaterialTheme.colors.secondaryVariant
        )
    }

    @Composable
    private fun FileTreeItem(fileDetail: Path) {
        var fileName = fileDetail.fileName.toString().take(18)

        if (fileDetail.fileName.toString().length > 18) {
            fileName = fileName.replaceRange(15, 17, ".")
        }

        Row( modifier = Modifier.padding(7.dp) ) {
            FileIcon(fileDetail)
            ClickableText(
                AnnotatedString(fileName),
                onClick = {
                    if (Files.isDirectory(fileDetail)) {
                        changePathUiCb(fileDetail)
                    }
                },
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 0.dp),
                style = TextStyle(color = Color.White)
            )
        }
    }

    @Composable
    fun FileTreeWidget(currentWorkingDir: Path) {
        val fileList = Files.list(currentWorkingDir)
            .collect(Collectors.toList())

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
