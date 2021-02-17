package com.kotlang.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.vectorXmlResource
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.kotlang.state.WindowState
import org.apache.commons.io.FilenameUtils

@Composable
fun FileIcon(fileDetail: Path) {
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
         modifier = Modifier.width(18.dp))
}

@Composable
fun FileTreeItem(fileDetail: Path) {
    var fileName = fileDetail.fileName.toString().take(18)

    if (fileDetail.fileName.toString().length > 18) {
        fileName = fileName.replaceRange(15, 17, ".")
    }

    Row( modifier = Modifier.padding(5.dp) ) {
        FileIcon(fileDetail)
        ClickableText(AnnotatedString(fileName), onClick = {
            if (Files.isDirectory(fileDetail)) {
                WindowState.selectedTab.changePath(fileDetail)
            }
        })
    }
}

@Composable
fun FileTree(currentPath: Path) {
    val fileList = Files.list(currentPath)
        .collect(Collectors.toList())

    LazyColumn(
        modifier = Modifier.width(220.dp).fillMaxHeight(),
    ) {
        itemsIndexed(fileList) { _, fileItem ->
            FileTreeItem(fileItem)
        }
    }
}
