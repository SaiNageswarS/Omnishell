package com.kotlang.ui

import androidx.compose.foundation.ClickableText
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.vectorXmlResource
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.kotlang.HistoryItem
import org.apache.commons.io.FilenameUtils
import kotlin.io.path.isDirectory

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
         modifier = Modifier.width(18.dp))
}

@Composable
fun FileTreeItem(fileDetail: Path, refreshShellTab: (Path, HistoryItem?) -> Unit) {
    var fileName = fileDetail.fileName.toString().take(18)

    if (fileDetail.fileName.toString().length > 18) {
        fileName = fileName.replaceRange(15, 17, ".")
    }

    Row( modifier = Modifier.padding(5.dp) ) {
        FileIcon(fileDetail)
        ClickableText(AnnotatedString(fileName), onClick = {
            if (Files.isDirectory(fileDetail)) {
                refreshShellTab(fileDetail, null)
            }
        })
    }
}

@Composable
fun FileTree(currentPath: Path, refreshShellTab: (Path, HistoryItem?) -> Unit) {
    val fileList = Files.list(currentPath)
        .collect(Collectors.toList())

    LazyColumnFor(items = fileList,
        modifier = Modifier.width(220.dp).fillMaxHeight(),
    ) { fileDetail ->
        FileTreeItem(fileDetail, refreshShellTab)
    }
}