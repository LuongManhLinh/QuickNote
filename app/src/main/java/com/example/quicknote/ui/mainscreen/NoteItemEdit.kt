package com.example.quicknote.ui.mainscreen

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.text.isDigitsOnly
import com.example.quicknote.R
import com.example.quicknote.data.entity.Key
import com.example.quicknote.data.entity.Note
import com.example.quicknote.data.entity.NoteContent
import com.example.quicknote.data.entity.NoteContentPresentation
import com.example.quicknote.ui.custom.CustomMoneyTextField
import com.example.quicknote.ui.custom.CustomTextField
import com.example.quicknote.ui.theme.QuickNoteTheme
import com.example.quicknote.util.formatNumberWithComma
import com.example.quicknote.util.removeCommaFromNumber
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
        placeholder = stringResource(R.string.link_placeholder),
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
    onMoneyChanged: (ULong) -> Unit,
) {

    var openDialog by rememberSaveable { mutableStateOf(false) }
    var isAdding by rememberSaveable { mutableStateOf(true) }
    var changingValue by rememberSaveable { mutableLongStateOf(0L) }


    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomMoneyTextField(
            modifier = Modifier,
            value = amount.toString(),
            onValueChanged = {
                val value = if (it.isEmpty() || !it.isDigitsOnly()) {
                    0u
                } else {
                    var parsed = it.toULongOrNull()
                    while (parsed == null) {
                        parsed = it.dropLast(1).toULongOrNull()
                    }
                    parsed
                }

                onMoneyChanged(value)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
//        CustomTextField(
//            modifier = Modifier,
//            value = amount.toString(),
//            onValueChanged = {
//                val value = if (it.isEmpty() || !it.isDigitsOnly()) {
//                    0u
//                } else {
//                    var parsed = it.toULongOrNull()
//                    while (parsed == null) {
//                        parsed = it.dropLast(1).toULongOrNull()
//                    }
//                    parsed
//                }
//
//                onMoneyChanged(value)
//            },
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Number,
//                imeAction = ImeAction.Done
//            )
//        )

        Text(
            text = " " + stringResource(R.string.money_UNIT),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.weight(1f))
        Icon(
            modifier = Modifier.clickable {
                openDialog = true
                isAdding = true
            },
            imageVector = Icons.Default.Add,
            contentDescription = null
        )
        Spacer(Modifier.padding(horizontal = dimensionResource(R.dimen.tiny)))
        Icon(
            modifier = Modifier.clickable {
                openDialog = true
                isAdding = false
            },
            imageVector = Icons.Default.Remove,
            contentDescription = null
        )
        Spacer(Modifier.padding(horizontal = dimensionResource(R.dimen.tiny)))
    }

    if (openDialog) {
        NoteItemEditMoneyDialog(
            currentMoney = amount,
            value = changingValue.toString(),
            onValueChanged = {
                val value = if (it.isEmpty() || !it.isDigitsOnly()) {
                    0L
                } else {
                    var parsed = it.toLongOrNull()
                    while (parsed == null) {
                        parsed = it.dropLast(1).toLongOrNull()
                    }
                    parsed
                }
                changingValue = value!!
            },
            isAdding = isAdding,
            onDone = {
                onMoneyChanged(
                    if (isAdding) {
                        amount + changingValue.toULong()
                    } else {
                        amount - changingValue.toULong()
                    }
                )
                openDialog = false
            },
            onDismissRequest = { openDialog = false }
        )
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