package com.example.quicknote.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.quicknote.data.constant.DatabaseConstant
import kotlinx.serialization.json.Json

@Entity(
    tableName = DatabaseConstant.NOTE_TABLE_NAME
)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val noteAsJson: String
) {
    fun toNote(): Note {
        val note = Json.decodeFromString<Note>(this.noteAsJson)
        note.noteId = id
        return note
    }
}