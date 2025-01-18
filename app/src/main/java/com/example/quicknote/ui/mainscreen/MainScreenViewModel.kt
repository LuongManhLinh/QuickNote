package com.example.quicknote.ui.mainscreen

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

    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                MainScreenUIState.fromNoteList(repository.getAll())
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
                isEditing = true
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
            1 -> NoteContent.Money(0u)
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

    fun onNoteChangeStateToEditing(noteIdx: Int) {
        noteEditingStack.add(_uiState.value.noteUIList[noteIdx].note)

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
    }

    fun onNoteEditingDone(noteIdx: Int) {
        noteEditingStack.clear()
        val note = _uiState.value.noteUIList[noteIdx].note

        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(note)
        }

        _uiState.update {
            it.copy(
                noteUIList = it.noteUIList.mapIndexed { idx, noteUIState ->
                    if (idx == noteIdx) {
                        noteUIState.copy(isEditing = false)
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
                noteUIList = it.noteUIList.mapIndexed { index, noteUIState ->
                    if (index == noteIdx) {
                        noteUIState.copy(isEditing = false)
                    } else {
                        noteUIState
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
        _uiState.update {
            it.copy(
                isSelectingNote = true
            )
        }
    }

    fun stopSelectingNote() {
        _uiState.update { state ->
            state.copy(
                isSelectingNote = false,
                noteUIList = state.noteUIList.map { it.copy(isSelected = false) }
            )
        }
    }

    fun selectNote(noteIdx: Int, isSelected: Boolean) {
        _uiState.update { state ->
            state.copy(
                noteUIList = state.noteUIList.mapIndexed { idx, noteUIState ->
                    if (idx == noteIdx) {
                        noteUIState.copy(isSelected = isSelected)
                    } else {
                        noteUIState
                    }
                },
                isAllSelected = state.noteUIList.all { it.isSelected }
            )
        }
    }

    fun deleteSelectedNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value.noteUIList.forEach {
                if (it.isSelected) {
                    repository.delete(it.note)
                }
            }

            _uiState.update { state ->
                state.copy(
                    noteUIList = state.noteUIList.filter { !it.isSelected }
                )
            }
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
    val isAllSelected: Boolean = false
) {
    companion object {
        fun fromNoteList(notes: List<Note>): MainScreenUIState {
            return MainScreenUIState(
                noteUIList = notes.map { NoteUIState(it) }
            )
        }

        fun toNoteList(uiState: MainScreenUIState): List<Note> {
            return uiState.noteUIList.map { it.note }
        }
    }
}

data class NoteUIState(
    val note: Note,
    val isEditing: Boolean = false,
    val isSelected: Boolean = false,
)

