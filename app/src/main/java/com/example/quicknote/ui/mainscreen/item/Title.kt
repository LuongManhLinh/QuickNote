package com.example.quicknote.ui.mainscreen.item

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun NoteTitle(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.titleLarge
    )
}