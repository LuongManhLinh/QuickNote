package com.example.quicknote.ui.mainscreen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.quicknote.R
import com.example.quicknote.ui.mainscreen.item.NoteItem

@Composable
internal fun NoteListSelection(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    noteUIList: List<NoteUIState>,
    onSelectionChanged: (Int, Boolean) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        items(noteUIList.size) { idx ->
            val noteUI = noteUIList[idx]
            NoteItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.small)),
                note = noteUI.note,
                isSelectingNote = true,
                isSelected = noteUI.isSelected,
                onSelectionChanged = { onSelectionChanged(idx, it) }
            )
        }

        item {
            val screenHeight = LocalConfiguration.current.screenHeightDp
            Spacer(
                modifier = Modifier
                    .height(screenHeight.dp / 2)
            )
        }
    }

}