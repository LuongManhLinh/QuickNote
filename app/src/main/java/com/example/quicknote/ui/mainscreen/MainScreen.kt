package com.example.quicknote.ui.mainscreen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.example.quicknote.ProjectViewModelProvider

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = ProjectViewModelProvider.provide(MainScreenViewModel::class)
) {
    val uiState = viewModel.uiState.collectAsState()
    Scaffold(
        bottomBar = {
            Row {
                Button(
                    onClick = { viewModel.addEditingNote() }
                ) {
                    Text(text = "Add Note")
                }

                Button(
                    onClick = { viewModel.deleteLastNote() }
                ) {
                    Text(text = "Delete Last Note")
                }
            }
        }
    ) { innerPadding ->
        if (uiState.value.noteUIList.isEmpty()) {
            Text(
                text = "No notes",
                modifier = modifier.padding(innerPadding)
            )
        } else {
            NoteList(
                modifier = modifier.padding(innerPadding),
                noteUIList = uiState.value.noteUIList,
                notePresentationList = viewModel.getAllNoteContentPresentation(),
                onNoteChanged = viewModel::onNoteChanged,
                onNoteContentAdded = viewModel::onNoteContentAdded,
                onNoteEditingCancel = viewModel::onNoteEditingCancel,
                onNoteEditingDone = viewModel::onNoteEditingDone
            )
        }
    }
}




