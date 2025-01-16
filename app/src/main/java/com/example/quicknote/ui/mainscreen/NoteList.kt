package com.example.quicknote.ui.mainscreen

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.quicknote.R
import com.example.quicknote.data.entity.Note
import com.example.quicknote.data.entity.NoteContentPresentation
import kotlinx.coroutines.coroutineScope

@Composable
internal fun NoteList(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    noteUIList: List<NoteUIState>,
    notePresentationList: List<NoteContentPresentation>,
    onNoteChanged: (Note, Int) -> Unit,
    onNoteContentAdded: (Int, Int) -> Unit,
    onNoteEditingUndo: (Int) -> Unit,
    onNoteEditingCancel: (Int) -> Unit,
    onNoteEditingDelete: (Int) -> Unit,
    onNoteEditingDone: (Int) -> Unit,
    onNoteLongPress: (Int) -> Unit,
    onDoubleTap: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        items(noteUIList.size) { idx ->
            val noteUI = noteUIList[idx]
            if (noteUI.isEditing) {
                NoteItemEdit(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.small)),
                    note = noteUI.note,
                    noteIdx = idx,
                    onNoteChanged = onNoteChanged,
                    notePresentationList = notePresentationList,
                    onNoteContentAdded = onNoteContentAdded,
                    onNoteEditingUndo = onNoteEditingUndo,
                    onNoteEditingCancel = onNoteEditingCancel,
                    onNoteEditingDelete = onNoteEditingDelete,
                    onNoteEditingDone = onNoteEditingDone
                )
            } else {
                NoteItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.small))
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onLongPress = { onNoteLongPress(idx) },
                                onDoubleTap = { onDoubleTap(idx) }
                            )
                        },
                    note = noteUI.note,
                    isSelectingNote = false
                )
            }
        }

        item {
            val screenHeight = LocalConfiguration.current.screenHeightDp
            Spacer(
                modifier = Modifier
                    .height(screenHeight.dp / 3)
            )
        }
    }
}
