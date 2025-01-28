package com.example.quicknote.ui.widget

import android.content.Context
import android.graphics.drawable.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.Action
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.material3.ColorProviders
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.quicknote.MainActivity
import com.example.quicknote.R
import com.example.quicknote.data.container.RoomNoteContainer
import com.example.quicknote.data.entity.Note
import com.example.quicknote.ui.theme.darkScheme
import com.example.quicknote.ui.theme.lightScheme
import com.example.quicknote.ui.widget.item.NoteItemWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class QuickNoteWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val noteList = RoomNoteContainer(context).noteRepository.getAll()
        provideContent {
            GlanceTheme(
                colors = ColorProviders(
                    light = lightScheme,
                    dark = darkScheme
                )
            ) {
                NoteList(
                    noteList = noteList,
                    onRefreshClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            update(context, id)
                        }
                    },
                    onEditClick = actionStartActivity<MainActivity>()
                )
            }
        }
    }
}

@Composable
private fun NoteList(
    modifier: GlanceModifier = GlanceModifier,
    noteList: List<Note>,
    onEditClick: Action,
    onRefreshClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = ColorProvider(
            GlanceTheme.colors.background.getColor(context).copy(alpha = 0.75f)
        ),
        titleBar = {
            Box(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 20.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        modifier = GlanceModifier,
                        text = "${noteList.size} ${context.getString(R.string.note).uppercase()}",
                        style = TextStyle(
                            fontSize = MaterialTheme.typography.titleMedium.fontSize
                        )
                    )
                }

                Row(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 20.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalAlignment = Alignment.End
                ) {
                    Image(
                        modifier = GlanceModifier
                            .size(24.dp)
                            .clickable(onClick = onEditClick),
                        provider = ImageProvider(
                            resId = R.drawable.widget_edit
                        ),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(
                            ColorProvider(
                                Color.Red
                            )
                        )
                    )
                    Box {
                        Spacer(modifier = GlanceModifier.size(16.dp))
                    }
                    Image(
                        modifier = GlanceModifier
                            .size(24.dp)
                            .clickable { onRefreshClick() },
                        provider = ImageProvider(
                            resId = R.drawable.widget_refresh,
                        ),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(
                            ColorProvider(Color.Red)
                        )
                    )
                }
            }
        }
    ) {
        LazyColumn(
            modifier = GlanceModifier
        ) {
            items(noteList) { note ->
                Column {
                    NoteItemWidget(
                        modifier = GlanceModifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        note = note
                    )

                    Spacer(modifier = GlanceModifier.size(16.dp))
                }
            }
        }
    }
}

