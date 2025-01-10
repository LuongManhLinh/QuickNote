package com.example.quicknote.data.entity

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Note(
    var title: String = "",
    var contents: List<NoteContent> = emptyList()
) {
    fun toEntity(): NoteEntity {
        return NoteEntity(
            noteAsJson = Json.encodeToString(this)
        )
    }
}