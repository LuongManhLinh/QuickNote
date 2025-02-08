package com.example.quicknote.ui.widget.item

import androidx.compose.material3.DividerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.size
import androidx.glance.unit.ColorProvider
import com.example.quicknote.data.entity.Note
import com.example.quicknote.data.entity.NoteContent

@Composable
fun NoteItemWidget(
    modifier: GlanceModifier = GlanceModifier,
    note: Note
) {
    val content = LocalContext.current

    Column(
        modifier = modifier
            .cornerRadius(12.dp)
            .background(
                ColorProvider(
                    GlanceTheme.colors.secondaryContainer.getColor(content).copy(alpha = 0.75f)
                )
            )
    ) {
        NoteTitleWidget(
            title = note.title
        )

        note.contents.forEach { content ->
            Column {
                Spacer(modifier = GlanceModifier.size(4.dp))

                HorizontalDividerWidget()

                Spacer(modifier = GlanceModifier.size(4.dp))

                when (content) {
                    is NoteContent.Text -> {
                        NoteContentTextWidget(
                            text = content.text
                        )
                    }

                    is NoteContent.Datetime -> {
                        NoteContentDatetimeWidget(
                            datetime = content.datetime
                        )
                    }
                    is NoteContent.KeyCombination -> {
                        NoteContentKeyCombinationWidget(
                            combination = content.combination
                        )
                    }
                    is NoteContent.Link -> {
                        NoteContentLinkWidget(
                            link = content.url
                        )
                    }
                    is NoteContent.Money -> {
                        NoteContentMoneyWidget(
                            amount = content.amount
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HorizontalDividerWidget(
    modifier: GlanceModifier = GlanceModifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(
                GlanceTheme.colors.outline
            )
    ) {

    }
}





