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
        return Json.decodeFromString<Note>(this.noteAsJson).also {
            it.noteId = id
        }
    }
}