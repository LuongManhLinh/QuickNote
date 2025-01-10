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
        val newNoteUI = NoteUIState(
            Note(),
            isEditing = true
        )
        _uiState.update {
            it.copy(
                noteUIList = it.noteUIList + newNoteUI
            )
        }
    }

    fun onNoteEditingDone(noteIdx: Int) {
        val note = uiState.value.noteUIList[noteIdx].note

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
        _uiState.update { state ->
            state.copy(
                noteUIList = state.noteUIList.filterIndexed { idx, _ -> idx != noteIdx }
            )
        }
    }

    fun deleteLastNote() {
        viewModelScope.launch {
            val notes = uiState.value.noteUIList
            if (notes.isNotEmpty()) {
                repository.delete(notes.last().note)
                loadNotes()
            }
        }

    }

    fun onNoteChanged(note: Note, noteIdx: Int) {
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
                            note = noteUIState.note.copy(contents = noteContents),
                            contentIdxToFocus = noteContents.size - 1
                        )
                    } else {
                        noteUIState
                    }
                }
            )
        }
    }
}

data class MainScreenUIState(
    val noteUIList: List<NoteUIState> = emptyList()
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
    val contentIdxToFocus: Int = -1
)

enum class MoneyUnit {
    UNIT,
    K,
    M,
}