package com.example.quicknote.data.entity

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Note(
    val title: String = "",
    val contents: List<NoteContent> = emptyList()
) {
    var noteId = 0L

    fun toNoteEntity(): NoteEntity {
        return NoteEntity(
            id = noteId,
            noteAsJson = Json.encodeToString(this)
        )
    }
}