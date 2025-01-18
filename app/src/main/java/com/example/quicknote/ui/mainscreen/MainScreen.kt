package com.example.quicknote.ui.mainscreen

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PathSegment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.quicknote.ProjectViewModelProvider
import com.example.quicknote.R
import com.example.quicknote.ui.theme.QuickNoteTheme
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = ProjectViewModelProvider.provide(MainScreenViewModel::class)
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            if (uiState.isSelectingNote) {
                MainScreenTopBar(
                    isAllSelected = uiState.isAllSelected,
                    onDelete = viewModel::deleteSelectedNotes,
                    onSelectAll = viewModel::selectAllNotes,
                    onCancel = viewModel::stopSelectingNote
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.addEditingNote()
                    coroutineScope.launch {
                        listState.animateScrollToItem(uiState.noteUIList.size)
                    }
                }
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        if (uiState.noteUIList.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_note),
                    style = MaterialTheme.typography.titleLarge
                )
            }

        } else {
            if (uiState.isSelectingNote) {
                NoteListSelection(
                    modifier = modifier.padding(innerPadding),
                    noteUIList = uiState.noteUIList,
                    onSelectionChanged = viewModel::selectNote
                )
            } else {
                NoteList(
                    modifier = modifier.padding(innerPadding),
                    listState = listState,
                    noteUIList = uiState.noteUIList,
                    notePresentationList = viewModel.getAllNoteContentPresentation(),
                    onNoteChanged = viewModel::onNoteChanged,
                    onNoteContentAdded = viewModel::onNoteContentAdded,
                    onNoteEditingUndo = viewModel::onNoteEditingUndo,
                    onNoteEditingCancel = viewModel::onNoteEditingCancel,
                    onNoteEditingDone = viewModel::onNoteEditingDone,
                    onNoteLongPress = {
                        coroutineScope.launch {
                            vibrate(context)
                        }
                        viewModel.startSelectingNote()
                        viewModel.selectNote(it, true)
                    },
                    onDoubleTap = viewModel::onNoteChangeStateToEditing
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenTopBar(
    modifier: Modifier = Modifier,
    isAllSelected: Boolean,
    onSelectAll: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onCancel: () -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(
                onClick = onCancel
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.cancel)
                )
            }
        },
        title = {
            MainScreenTopBarContent(
                isAllSelected = isAllSelected,
                onSelectAll = onSelectAll,
                onDelete = onDelete
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )

    )

}

@Composable
private fun MainScreenTopBarContent(
    modifier: Modifier = Modifier,
    isAllSelected: Boolean,
    onSelectAll: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isAllSelected,
            onCheckedChange = onSelectAll
        )
        Text(
            text = stringResource(R.string.select_all),
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(Modifier.weight(1f))
        Button(
            onClick = onDelete,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )
                Spacer(Modifier.padding(horizontal = dimensionResource(R.dimen.small)))
                Text(text = stringResource(R.string.delete))
            }
        }
        Spacer(Modifier.padding(horizontal = dimensionResource(R.dimen.tiny)))
    }
}



private fun vibrate(
    context: Context,
    duration: Long = 500,
    amplitude: Int = VibrationEffect.DEFAULT_AMPLITUDE
) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(VibratorManager::class.java)
        vibratorManager?.defaultVibrator
    } else {
        context.getSystemService(Vibrator::class.java)
    }

    vibrator?.let {
        val vibrationEffect = VibrationEffect.createOneShot(duration, amplitude)
        it.vibrate(vibrationEffect)
    }
}

@Preview(showBackground = true)
@Composable
private fun TopBarPreview() {
    QuickNoteTheme {
        MainScreenTopBar(
            isAllSelected = true,
            onSelectAll = {},
            onDelete = {},
            onCancel = {}
        )
    }
}



