package com.example.quicknote.ui.widget.item

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.text.Text
import androidx.glance.text.TextDecoration
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.quicknote.R

@Composable
fun NoteContentLinkWidget(
    modifier: GlanceModifier = GlanceModifier,
    link: String
) {
    val isLinkEmpty = link.isEmpty()
    val context = LocalContext.current
    Text(
        modifier = modifier,
        text = if (isLinkEmpty) {
            context.getString(R.string.empty_link)
        } else {
            link
        },
        style = TextStyle(
            textDecoration = if (isLinkEmpty) {
                TextDecoration.None
            } else {
                TextDecoration.Underline
            },
            color = if (isLinkEmpty) {
                GlanceTheme.colors.onBackground
            } else {
                ColorProvider(Color(0xFF1f6bdc))
            }
        ),
    )
}