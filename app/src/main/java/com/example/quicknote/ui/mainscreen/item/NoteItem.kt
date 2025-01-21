package com.example.quicknote.ui.mainscreen.item

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
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
fun AnimatedNoteItem(
    modifier: Modifier = Modifier,
    note: Note,
    isSelectingNote: Boolean = false,
    isSelected: Boolean = false,
    onSelectionChanged: (Boolean) -> Unit = {}
) {
    var isShowingTopBar by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
    ) {
        AnimatedNoteTopBar(
            modifier = Modifier.padding(
                horizontal = dimensionResource(R.dimen.small)
            ),
            creationDate = LocalDate.now(),
            onEditingStarted = {},
            isShowing = isShowingTopBar
        )
        NoteItemContent(
            modifier = Modifier
                .pointerInput(Unit){
                    detectTapGestures(
                        onTap = { isShowingTopBar = true}
                    )
                },
            note = note,
            isSelectingNote = isSelectingNote,
            isSelected = isSelected,
            onSelectionChanged = onSelectionChanged
        )
    }

    LaunchedEffect(isShowingTopBar) {
        if (isShowingTopBar) {
            coroutineScope.launch {
                delay(1500)
                isShowingTopBar = false
            }
        }
    }
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

@Composable
private fun AnimatedNoteTopBar(
    modifier: Modifier = Modifier,
    creationDate: LocalDate,
    onEditingStarted: () -> Unit,
    isShowing: Boolean
) {
    Box(
        modifier = modifier.heightIn(
            min = dimensionResource(R.dimen.normal_medium)
        ),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = isShowing,
            enter = fadeIn(
                animationSpec = tween(1000)
            ),
            exit = fadeOut(
                animationSpec = tween(1000)
            )
        ) {
            NoteTopBar(
                creationDate = creationDate,
                onEditingStarted = onEditingStarted
            )
        }
    }

}

@Composable
private fun NoteTopBar(
    modifier: Modifier = Modifier,
    creationDate: LocalDate,
    onEditingStarted: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.creation_date, formatDate(creationDate)),
            fontStyle = FontStyle.Italic
        )
        Spacer(Modifier.weight(1f))

        Icon(
            modifier = Modifier.clickable { onEditingStarted() },
            imageVector = Icons.Default.EditNote,
            contentDescription = null
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun NoteTopPreview() {
    QuickNoteTheme {
        NoteTopBar(
            creationDate = LocalDate.now(),
            onEditingStarted = {}
        )
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















