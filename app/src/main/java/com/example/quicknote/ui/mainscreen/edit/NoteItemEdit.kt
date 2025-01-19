package com.example.quicknote.ui.mainscreen.edit

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.quicknote.R
import com.example.quicknote.data.entity.Key
import com.example.quicknote.data.entity.Note
import com.example.quicknote.data.entity.NoteContent
import com.example.quicknote.data.entity.NoteContentPresentation
import com.example.quicknote.ui.theme.QuickNoteTheme
import java.time.LocalDate

@Composable
internal fun NoteItemEdit(
    modifier: Modifier = Modifier,
    note: Note,
    noteIdx: Int,
    onNoteChanged: (Note, Int) -> Unit,
    notePresentationList: List<NoteContentPresentation>,
    onNoteContentAdded: (Int, Int) -> Unit,
    onNoteEditingUndo: (Int) -> Unit,
    onNoteEditingCancel: (Int) -> Unit,
    onNoteEditingDone: (Int) -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        NoteItemEditTopButtons(
            modifier = Modifier
                .align(Alignment.End)
                .padding(bottom = dimensionResource(R.dimen.small)),
            onUndo = { onNoteEditingUndo(noteIdx) },
            onCancelled = { onNoteEditingCancel(noteIdx) },
            onDone = { onNoteEditingDone(noteIdx) }
        )
        NoteItemEditContent(
            modifier = Modifier.fillMaxWidth(),
            note = note,
            onNoteChanged = { onNoteChanged(it, noteIdx) }
        )
        NoteItemEditBottomButtons(
            notePresentationList = notePresentationList,
            onTypeIdClicked = {
                onNoteContentAdded(noteIdx, it)
            }
        )
    }
}

@Composable
private fun NoteItemEditContent(
    modifier: Modifier = Modifier,
    note: Note,
    onNoteChanged: (Note) -> Unit,
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(
                    top = dimensionResource(R.dimen.small),
                    bottom = dimensionResource(R.dimen.small),
                    start = dimensionResource(R.dimen.small)
                ),
            verticalArrangement = Arrangement.Center,
        ) {
            NoteTitleEdit(
                title = note.title,
                onTitleChanged = { title ->
                    onNoteChanged(note.copy(title = title))
                }
            )

            note.contents.forEachIndexed { idx, content ->
                HorizontalDivider(Modifier.padding(vertical = dimensionResource(R.dimen.small)))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NoteItemEditMainContent(
                        modifier = Modifier.weight(1f),
                        note = note,
                        contentIdx = idx,
                        content = content,
                        onNoteChanged = onNoteChanged
                    )
                    
                    Row(
                        modifier = Modifier.padding(
                            start = dimensionResource(R.dimen.tiny),
                            end = dimensionResource(R.dimen.small)
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                onNoteChanged(
                                    note.copy(
                                        contents = note.contents.toMutableList().apply {
                                            removeAt(idx)
                                        }
                                    )
                                )
                            }
                        )
                        Spacer(Modifier.padding(horizontal = dimensionResource(R.dimen.tiny)))
                        Column {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    if (idx > 0) {
                                        onNoteChanged(
                                            note.copy(
                                                contents = note.contents.toMutableList().apply {
                                                    val temp = this[idx]
                                                    this[idx] = this[idx - 1]
                                                    this[idx - 1] = temp
                                                }
                                            )
                                        )
                                    }
                                }
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    if (idx < note.contents.size - 1) {
                                        onNoteChanged(
                                            note.copy(
                                                contents = note.contents.toMutableList().apply {
                                                    val temp = this[idx]
                                                    this[idx] = this[idx + 1]
                                                    this[idx + 1] = temp
                                                }
                                            )
                                        )
                                    }
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}

@Composable
private fun NoteItemEditMainContent(
    modifier: Modifier = Modifier,
    note: Note,
    contentIdx: Int,
    content: NoteContent,
    onNoteChanged: (Note) -> Unit
) {
    when (content) {
        is NoteContent.Text -> {
            NoteContentTextEdit(
                modifier = modifier,
                text = content.text,
                onTextChanged = { text ->
                    onNoteChanged(
                        note.copy(
                            contents = note.contents.toMutableList().apply {
                                this[contentIdx] = content.copy(text = text)
                            }
                        )
                    )
                }
            )
        }

        is NoteContent.Datetime -> {
            NoteContentDatetimeEdit(
                modifier = modifier,
                datetime = content.datetime,
                onDatetimeChanged = {
                    onNoteChanged(
                        note.copy(
                            contents = note.contents.toMutableList().apply {
                                this[contentIdx] = content.copy(datetime = it)
                            }
                        )
                    )
                }
            )
        }

        is NoteContent.KeyCombination -> {
            NoteContentKeyCombinationEdit(
                modifier = modifier,
                combination = content.combination,
                onKeyListChange = { combination ->
                    onNoteChanged(
                        note.copy(
                            contents = note.contents.toMutableList().apply {
                                this[contentIdx] = content.copy(
                                    combination = combination
                                )
                            }
                        )
                    )
                }
            )
        }

        is NoteContent.Link -> {
            NoteContentLinkEdit(
                modifier = modifier,
                link = content.url,
                onLinkChanged = { link ->
                    onNoteChanged(
                        note.copy(
                            contents = note.contents.toMutableList().apply {
                                this[contentIdx] = content.copy(url = link)
                            }
                        )
                    )
                }
            )
        }

        is NoteContent.Money -> {
            NoteContentMoneyEdit(
                modifier = modifier,
                amount = content.amount,
                onMoneyChanged = { amount ->
                    onNoteChanged(
                        note.copy(
                            contents = note.contents.toMutableList().apply {
                                this[contentIdx] = content.copy(amount = amount)
                            }
                        )
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NoteItemEditBottomButtons(
    modifier: Modifier = Modifier,
    notePresentationList: List<NoteContentPresentation>,
    onTypeIdClicked: (Int) -> Unit,
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        notePresentationList.forEachIndexed { idx, presentation ->
            EditActionButton(
                modifier = Modifier.padding(top = dimensionResource(R.dimen.small)),
                onClick = { onTypeIdClicked(idx) },
                iconId = presentation.iconId,
                textId = presentation.titleId
            )
        }
    }
}


@Composable
private fun NoteItemEditTopButtons(
    modifier: Modifier = Modifier,
    onUndo: () -> Unit,
    onCancelled: () -> Unit,
    onDone: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        EditActionButton(
            onClick = onUndo,
            iconId = R.drawable.ic_undo,
            textId = R.string.undo,
        )
        Spacer(Modifier.padding(horizontal = dimensionResource(R.dimen.tiny)))
        EditActionButton(
            onClick = onCancelled,
            iconId = R.drawable.ic_cancel,
            textId = R.string.cancel,
        )
        Spacer(Modifier.padding(horizontal = dimensionResource(R.dimen.tiny)))
        EditActionButton(
            onClick = onDone,
            iconId = R.drawable.ic_done,
            textId = R.string.done,
            iconTint = if (isSystemInDarkTheme()) {
                colorResource(R.color.done_on_dark)
            } else {
                colorResource(R.color.done_on_light)
            },
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    }
}

@Composable
private fun EditActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    @DrawableRes iconId: Int,
    @StringRes textId: Int,
    iconTint: Color = Color.Black,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        border = BorderStroke(
            dimensionResource(R.dimen.micro),
            MaterialTheme.colorScheme.primary
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.tiny))
                .clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(dimensionResource(R.dimen.normal)),
                painter = painterResource(iconId),
                contentDescription = stringResource(textId),
                tint = iconTint
            )
            Spacer(Modifier.padding(horizontal = dimensionResource(R.dimen.tiny)))
            Text(
                text = stringResource(textId),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    QuickNoteTheme {
        NoteItemEdit(
            note = Note(
                title = "Title",
                contents = listOf(
                    NoteContent.Text("This is a very long text so that it shows how the text wraps around the screen"),
                    NoteContent.Datetime(LocalDate.now()),
                    NoteContent.Link("https://www.google.com"),
                    NoteContent.KeyCombination(listOf(Key.KeyText("Ctrl"))),
                    NoteContent.Money(10000u)
                )
            ),
            noteIdx = 0,
            onNoteContentAdded = {_, _ -> },
            notePresentationList = NoteContentPresentation.allTypes,
            onNoteChanged = {_, _ ->},
            onNoteEditingUndo = {},
            onNoteEditingCancel = {},
            onNoteEditingDone = {}
        )
    }
}