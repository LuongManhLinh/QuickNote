package com.example.quicknote.ui.mainscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quicknote.data.entity.Note
import com.example.quicknote.data.entity.NoteContent
import com.example.quicknote.data.entity.NoteContentPresentation
import com.example.quicknote.data.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainScreenViewModel(
    private val repository: NoteRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(MainScreenUIState())
    val uiState = _uiState.asStateFlow()

    private val noteEditingStack = mutableListOf<Note>()
    private var cacheNoteUIList: List<NoteUIState>? = null

    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    noteUIList = repository.getAll().map { note ->
                        NoteUIState(note)
                    }
                )
            }
        }
    }

    fun getAllNoteContentPresentation(): List<NoteContentPresentation> {
        return NoteContentPresentation.allTypes
    }

    fun addEditingNote() {
        viewModelScope.launch {
            val newNoteUI = NoteUIState(
                Note(),
                isEditing = true,
                isNew = true
            )

            noteEditingStack.add(newNoteUI.note)

            _uiState.update {
                it.copy(
                    noteUIList = it.noteUIList + newNoteUI
                )
            }
        }
    }

    fun onNoteChanged(note: Note, noteIdx: Int) {
        noteEditingStack.add(_uiState.value.noteUIList[noteIdx].note)

        _uiState.update {
            it.copy(
                noteUIList = it.noteUIList.mapIndexed { idx, noteUIState ->
                    if (idx == noteIdx) {
                        noteUIState.copy(note = note)
                    } else {
                        noteUIState
                    }
                }
            )
        }
    }

    fun onNoteContentAdded(noteIdx: Int, typeId: Int) {
        noteEditingStack.add(_uiState.value.noteUIList[noteIdx].note)

        val content = when (typeId) {
            0 -> NoteContent.Text("")
            1 -> NoteContent.Money(0L)
            2 -> NoteContent.Datetime(LocalDate.now())
            3 -> NoteContent.Link("")
            4 -> NoteContent.KeyCombination(emptyList())
            else -> throw IllegalArgumentException("Unknown type id: $typeId")
        }

        _uiState.update {
            it.copy(
                noteUIList = it.noteUIList.mapIndexed { idx, noteUIState ->
                    if (idx == noteIdx) {
                        val noteContents = noteUIState.note.contents.toMutableList()
                        noteContents.add(content)
                        noteUIState.copy(
                            note = noteUIState.note.copy(contents = noteContents)
                        )
                    } else {
                        noteUIState
                    }
                }
            )
        }
    }

    fun changeNoteStateToEditing(noteIdx: Int) {
        _uiState.update {
            it.copy(
                noteUIList = it.noteUIList.mapIndexed { idx, noteUIState ->
                    if (idx == noteIdx) {
                        noteUIState.copy(isEditing = true)
                    } else {
                        if (noteUIState.isEditing) {
                            onNoteEditingDone(idx)
                            noteUIState.copy(isEditing = false)
                        } else {
                            noteUIState
                        }
                    }
                }
            )
        }

        noteEditingStack.add(_uiState.value.noteUIList[noteIdx].note)
    }

    fun onNoteEditingDone(noteIdx: Int) {
        noteEditingStack.clear()
        val noteUIState = _uiState.value.noteUIList[noteIdx]

        viewModelScope.launch(Dispatchers.IO) {
            if (noteUIState.isNew) {
                repository.insert(note = noteUIState.note)
            } else {
                repository.update(note = noteUIState.note)
            }
        }

        _uiState.update {
            it.copy(
                noteUIList = it.noteUIList.mapIndexed { idx, noteUIState ->
                    if (idx == noteIdx) {
                        noteUIState.copy(
                            isEditing = false,
                            isNew = false
                        )
                    } else {
                        noteUIState
                    }
                }
            )
        }
    }

    fun onNoteEditingCancel(noteIdx: Int) {
        noteEditingStack.clear()

        _uiState.update {
            it.copy(
                noteUIList = if (it.noteUIList[noteIdx].isNew) {
                    it.noteUIList.toMutableList().apply { removeAt(noteIdx) }
                } else {
                    it.noteUIList.mapIndexed { index, noteUIState ->
                        if (index == noteIdx) {
                            noteUIState.copy(isEditing = false)
                        } else {
                            noteUIState
                        }
                    }
                }
            )
        }
    }

    fun onNoteEditingUndo(noteIdx: Int) {
        if (noteEditingStack.isNotEmpty()) {
            val note = noteEditingStack.removeAt(noteEditingStack.size - 1)
            _uiState.update {
                it.copy(
                    noteUIList = it.noteUIList.mapIndexed { idx, noteUIState ->
                        if (idx == noteIdx) {
                            noteUIState.copy(note = note)
                        } else {
                            noteUIState
                        }
                    }
                )
            }
        }
    }

    fun startSelectingNote() {
        _uiState.update { state ->
            state.copy(
                isSelectingNote = true,
                noteUIList = state.noteUIList.map { it.copy(isSelected = false) }
            )
        }
    }

    fun stopSelectingNote() {
        _uiState.update { state ->
            state.copy(
                isSelectingNote = false,
            )
        }
    }

    fun selectNote(noteIdx: Int, isSelected: Boolean) {
        _uiState.update { state ->
            val newNoteUIList = state.noteUIList.mapIndexed { idx, noteUIState ->
                if (idx == noteIdx) {
                    noteUIState.copy(isSelected = isSelected)
                } else {
                    noteUIState
                }
            }
            state.copy(
                noteUIList = newNoteUIList,
                isAllSelected = newNoteUIList.all { it.isSelected }
            )
        }
    }

    fun deleteSelectedNotes(): Int {
        val selectedNotes = _uiState.value.noteUIList.filter { it.isSelected }
        if (selectedNotes.isEmpty()) {
            return 0
        }

        cacheNoteUIList = selectedNotes
        viewModelScope.launch(Dispatchers.IO) {
            selectedNotes.forEach { noteUIState ->
                repository.delete(noteUIState.note)
            }
            loadNotes()
        }

        return selectedNotes.size
    }

    fun undoDeleteNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            cacheNoteUIList?.forEach { noteUIState ->
                repository.insert(noteUIState.note)
            }
            loadNotes()
            cacheNoteUIList = null
        }
    }

    fun selectAllNotes(isSelected: Boolean) {
        _uiState.update { state ->
            state.copy(
                noteUIList = state.noteUIList.map { it.copy(isSelected = isSelected) },
                isAllSelected = isSelected
            )
        }
    }
}

data class MainScreenUIState(
    val noteUIList: List<NoteUIState> = emptyList(),
    val isSelectingNote: Boolean = false,
    val isAllSelected: Boolean = false,
)

data class NoteUIState(
    val note: Note,
    val isEditing: Boolean = false,
    val isSelected: Boolean = false,
    val isNew: Boolean = false
)
