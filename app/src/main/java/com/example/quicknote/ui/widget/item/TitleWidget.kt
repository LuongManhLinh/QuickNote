package com.example.quicknote.ui.widget.item

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.quicknote.R

@Composable
fun NoteTitleWidget(
    modifier: GlanceModifier = GlanceModifier,
    title: String
) {
    val context = LocalContext.current
    Text(
        modifier = modifier,
        text = title.ifEmpty { context.getString(R.string.untitled) },
        style = TextStyle(
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
        ),
    )
}