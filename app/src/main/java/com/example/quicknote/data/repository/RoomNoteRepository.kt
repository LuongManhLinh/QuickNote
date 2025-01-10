package com.example.quicknote.data.repository

import com.example.quicknote.data.database.NoteDao
import com.example.quicknote.data.entity.Note

class RoomNoteRepository(
    private val noteDao: NoteDao
): NoteRepository {
    override suspend fun getAll(): List<Note> {
        return noteDao.getAll().map { it.toNote() }
    }

    override suspend fun insert(note: Note) {
        noteDao.insert(note.toEntity())
    }

    override suspend fun update(note: Note) {
        noteDao.update(note.toEntity())
    }

    override suspend fun delete(note: Note) {
        noteDao.delete(note.toEntity())
    }

}