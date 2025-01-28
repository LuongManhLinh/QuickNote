package com.example.quicknote.ui.widget.item

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.text.Text
import com.example.quicknote.R

@Composable
fun NoteContentTextWidget(
    modifier: GlanceModifier = GlanceModifier,
    text: String
) {
    val context = LocalContext.current
    Text(
        modifier = modifier,
        text = text.ifEmpty { context.getString(R.string.empty_text) },
    )
}