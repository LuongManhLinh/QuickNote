package com.example.quicknote.ui.mainscreen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.quicknote.ProjectViewModelProvider
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = ProjectViewModelProvider.provide(MainScreenViewModel::class)
) {
    val uiState = viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.addEditingNote()
                    coroutineScope.launch {
                        listState.animateScrollToItem(uiState.value.noteUIList.size)
                    }
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
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
                listState = listState,
                noteUIList = uiState.value.noteUIList,
                notePresentationList = viewModel.getAllNoteContentPresentation(),
                onNoteChanged = viewModel::onNoteChanged,
                onNoteContentAdded = viewModel::onNoteContentAdded,
                onNoteEditingCancel = viewModel::onNoteEditingCancel,
                onNoteEditingDone = viewModel::onNoteEditingDone,
                onNoteLongPress = viewModel::onNoteChangeStateToEditing
            )
        }
    }
}




