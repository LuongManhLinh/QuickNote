package com.example.quicknote.ui.mainscreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.quicknote.R
import com.example.quicknote.data.entity.Note
import com.example.quicknote.data.entity.NoteContentPresentation

@Composable
internal fun NoteList(
    modifier: Modifier = Modifier,
    noteUIList: List<NoteUIState>,
    notePresentationList: List<NoteContentPresentation>,
    onNoteChanged: (Note, Int) -> Unit,
    onNoteContentAdded: (Int, Int) -> Unit,
    onNoteEditingCancel: (Int) -> Unit,
    onNoteEditingDone: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier
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
                    contentIdxToFocus = noteUI.contentIdxToFocus,
                    onNoteChanged = onNoteChanged,
                    notePresentationList = notePresentationList,
                    onNoteContentAdded = onNoteContentAdded,
                    onNoteEditingCancel = onNoteEditingCancel,
                    onNoteEditingDone = onNoteEditingDone
                )
            } else {
                NoteItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.small)),
                    note = noteUI.note
                )
            }
        }
    }
}
