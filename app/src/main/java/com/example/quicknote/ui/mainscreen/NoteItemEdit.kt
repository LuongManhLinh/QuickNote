package com.example.quicknote.ui.mainscreen

import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.example.quicknote.R
import com.example.quicknote.data.entity.Key
import com.example.quicknote.data.entity.Note
import com.example.quicknote.data.entity.NoteContent
import com.example.quicknote.data.entity.NoteContentPresentation
import com.example.quicknote.ui.custom.CustomTextField
import com.example.quicknote.ui.theme.QuickNoteTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

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
    onNoteEditingDelete: (Int) -> Unit,
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
            onDelete = { onNoteEditingDelete(noteIdx) },
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
                combination = content.combination
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
                unit = MoneyUnit.K
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
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        notePresentationList.forEachIndexed { idx, presentation ->
            EditActionButton(
                modifier = Modifier.padding(top = dimensionResource(R.dimen.small)),
                onClick = { onTypeIdClicked(idx) },
                iconId = presentation.iconId,
                text = presentation.text
            )
        }
    }
}


@Composable
private fun NoteItemEditTopButtons(
    modifier: Modifier = Modifier,
    onUndo: () -> Unit,
    onCancelled: () -> Unit,
    onDelete: () -> Unit,
    onDone: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        EditActionButton(
            onClick = onUndo,
            iconId = R.drawable.ic_undo,
            text = stringResource(R.string.undo),
        )
        Spacer(Modifier.padding(horizontal = dimensionResource(R.dimen.tiny)))
        EditActionButton(
            onClick = onCancelled,
            iconId = R.drawable.ic_cancel,
            text = stringResource(R.string.cancel),
        )
        Spacer(Modifier.padding(horizontal = dimensionResource(R.dimen.tiny)))
        EditActionButton(
            onClick = onDelete,
            iconId = R.drawable.ic_delete,
            text = stringResource(R.string.delete),
            iconTint = MaterialTheme.colorScheme.error,
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
        Spacer(Modifier.padding(horizontal = dimensionResource(R.dimen.tiny)))
        EditActionButton(
            onClick = onDone,
            iconId = R.drawable.ic_done,
            text = stringResource(R.string.done),
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
    text: String,
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
                contentDescription = text,
                tint = iconTint
            )
            Spacer(Modifier.padding(horizontal = dimensionResource(R.dimen.tiny)))
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun NoteTitleEdit(
    modifier: Modifier = Modifier,
    title: String,
    onTitleChanged: (String) -> Unit
) {
    CustomTextField(
        modifier = modifier,
        value = title,
        onValueChanged = onTitleChanged,
        textStyle = MaterialTheme.typography.titleLarge,
        placeholder = stringResource(R.string.title_placeholder)
    )

}


@Composable
private fun NoteContentTextEdit(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit
) {
    CustomTextField(
        modifier = modifier,
        value = text,
        onValueChanged = onTextChanged,
        textStyle = MaterialTheme.typography.bodyLarge,
        placeholder = stringResource(R.string.text_placeholder)
    )
}


@Composable
private fun NoteContentDatetimeEdit(
    modifier: Modifier = Modifier,
    datetime: LocalDate,
    onDatetimeChanged: (LocalDate) -> Unit,
) {
    val today = LocalDate.now()
    val dateToShow = when (datetime) {
        today -> {
            stringResource(R.string.today)
        }
        today.minusDays(1) -> {
            stringResource(R.string.yesterday)
        }
        today.plusDays(1) -> {
            stringResource(R.string.tomorrow)
        }
        in today.plusDays(2)..today.plusDays(7) -> {
            stringResource(R.string.n_day_after, today.until(datetime).days)
        }
        in today.minusDays(7)..today.minusDays(2) -> {
            stringResource(R.string.n_day_before, datetime.until(today).days)
        }
        else -> {
            datetime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        }
    }

    var isShowingDatePicker by remember { mutableStateOf(false) }

    Text(
        modifier = modifier.clickable { isShowingDatePicker = true },
        text = dateToShow,
        style = MaterialTheme.typography.bodyLarge
    )

    if (isShowingDatePicker) {
        DatePickerModal(
            onDateMillisSelected = { selectedDateMillis ->
                if (selectedDateMillis != null) {
                    onDatetimeChanged(LocalDate.ofEpochDay(selectedDateMillis / 86400000))
                }
            },
            onDismiss = { isShowingDatePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerModal(
    onDateMillisSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateMillisSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text(stringResource(R.string.done))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}


@Composable
private fun NoteContentKeyCombinationEdit(
    modifier: Modifier = Modifier,
    combination: List<Key>
) {
    val keyAsAny: List<Any> = combination.flatMap { listOf(it, "+") }.dropLast(1)

    LazyRow(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(keyAsAny) { key ->
            when (key) {
                is Key.KeyText -> {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        ),
                        border = BorderStroke(
                            dimensionResource(R.dimen.micro),
                            MaterialTheme.colorScheme.tertiary
                        ),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.tiny_small))
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(dimensionResource(R.dimen.tiny)),
                            text = key.text,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                is Key.KeySymbol -> {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        ),
                        border = BorderStroke(
                            dimensionResource(R.dimen.micro),
                            MaterialTheme.colorScheme.tertiary
                        ),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.tiny_small))
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(dimensionResource(R.dimen.key_sym_size))
                                .padding(dimensionResource(R.dimen.tiny)),
                            painter = painterResource(key.iconId),
                            contentDescription = key.contentDescription
                        )
                    }
                }

                is String -> {
                    Text(
                        modifier = Modifier.padding(dimensionResource(R.dimen.tiny)),
                        text = key,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}


@Composable
private fun NoteContentLinkEdit(
    modifier: Modifier = Modifier,
    link: String,
    onLinkChanged: (String) -> Unit
) {
    CustomTextField(
        modifier = modifier,
        value = link,
        onValueChanged = onLinkChanged,
        textStyle = TextStyle(
            color = if (isSystemInDarkTheme()) {
                colorResource(R.color.link_on_dark)
            } else {
                colorResource(R.color.link_on_light)
            },
            textDecoration = TextDecoration.Underline,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontWeight = FontWeight.Normal
        )
    )
}



@Composable
private fun NoteContentMoneyEdit(
    modifier: Modifier = Modifier,
    amount: ULong,
    unit: MoneyUnit
) {
    val amountString: String
    val unitString: String
    when (unit) {
        MoneyUnit.UNIT -> {
            amountString = amount.toString()
            unitString = stringResource(R.string.money_UNIT)
        }
        MoneyUnit.K -> {
            amountString = if ((amount % 1000u).toUInt() == 0u) {
                (amount / 1000u).toString()
            } else {
                String
                    .format(Locale.getDefault(), "%.2f", amount.toFloat() / 1000)
            }
            unitString = stringResource(R.string.money_K)
        }
        MoneyUnit.M -> {
            amountString = if ((amount % 1000000u).toUInt() == 0u) {
                (amount / 1000000u).toString()
            } else {
                String
                    .format(Locale.getDefault(), "%.2f", amount.toFloat() / 1000000)
            }
            unitString = stringResource(R.string.money_M)
        }
    }

    Text(
        modifier = modifier,
        text = "$amountString $unitString",
        style = MaterialTheme.typography.bodyLarge
    )
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
                    NoteContent.Money(1000u)
                )
            ),
            noteIdx = 0,
            onNoteContentAdded = {_, _ -> },
            notePresentationList = NoteContentPresentation.allTypes,
            onNoteChanged = {_, _ ->},
            onNoteEditingUndo = {},
            onNoteEditingDelete = {},
            onNoteEditingCancel = {},
            onNoteEditingDone = {}
        )
    }
}