package com.example.quicknote.data.repository

import android.util.Log
import com.example.quicknote.data.database.NoteDao
import com.example.quicknote.data.entity.Note

class RoomNoteRepository(
    private val noteDao: NoteDao
): NoteRepository {
    override suspend fun getAll(): List<Note> {
        return noteDao.getAll().map { it.toNote() }
    }

    override suspend fun insert(note: Note) {
        noteDao.insert(note.toNoteEntity())
    }

    override suspend fun update(note: Note) {
        Log.e("RoomNoteRepository", "update: ${note.noteId}")
        noteDao.update(note.toNoteEntity())
    }

    override suspend fun delete(note: Note) {
        noteDao.delete(note.toNoteEntity())
    }

}