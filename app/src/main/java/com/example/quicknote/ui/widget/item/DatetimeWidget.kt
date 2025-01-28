package com.example.quicknote.ui.widget.item

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.text.Text
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun NoteContentDatetimeWidget(
    modifier: GlanceModifier = GlanceModifier,
    datetime: LocalDate
) {
    val dateToShow = datetime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))

    Text(
        modifier = modifier,
        text = dateToShow
    )
}