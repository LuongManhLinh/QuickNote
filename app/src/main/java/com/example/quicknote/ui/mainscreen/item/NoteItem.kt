package com.example.quicknote.ui.mainscreen.item

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.quicknote.R
import com.example.quicknote.data.entity.Note
import com.example.quicknote.data.entity.NoteContent
import com.example.quicknote.ui.theme.QuickNoteTheme
import com.example.quicknote.util.formatDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: Note,
    isSelectingNote: Boolean = false,
    isSelected: Boolean = false,
    onSelectionChanged: (Boolean) -> Unit = {}
) {
    NoteItemContent(
        modifier = modifier,
        note = note,
        isSelectingNote = isSelectingNote,
        isSelected = isSelected,
        onSelectionChanged = onSelectionChanged
    )
}

@Composable
private fun NoteItemContent(
    modifier: Modifier = Modifier,
    note: Note,
    isSelectingNote: Boolean = false,
    isSelected: Boolean = false,
    onSelectionChanged: (Boolean) -> Unit = {}
) {
    Card(
        modifier = modifier
            .then(
                if (isSelectingNote) {
                    Modifier.clickable {
                        onSelectionChanged(!isSelected)
                    }
                } else {
                    Modifier
                }
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(dimensionResource(R.dimen.small)),
                verticalArrangement = Arrangement.Center,
            ) {
                NoteTitle(
                    title = note.title
                )

                note.contents.forEach { content ->
                    HorizontalDivider(Modifier.padding(vertical = dimensionResource(R.dimen.small)))

                    when (content) {
                        is NoteContent.Text -> {
                            NoteContentText(
                                text = content.text
                            )
                        }

                        is NoteContent.Datetime -> {
                            NoteContentDatetime(
                                datetime = content.datetime
                            )
                        }

                        is NoteContent.KeyCombination -> {
                            NoteContentKeyCombination(
                                combination = content.combination
                            )
                        }

                        is NoteContent.Link -> {
                            NoteContentLink(
                                link = content.url
                            )
                        }

                        is NoteContent.Money -> {
                            NoteContentMoney(
                                amount = content.amount,
                            )
                        }
                    }
                }
            }

            if (isSelectingNote) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = onSelectionChanged
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun NoteItemPreview() {
    QuickNoteTheme {
        NoteItem(
            note = Note(
                title = "Title",
                contents = listOf(
                    NoteContent.Text("Text"),
                    NoteContent.Datetime(LocalDate.now())
                )
            )
        )
    }
}















