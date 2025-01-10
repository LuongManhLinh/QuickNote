package com.example.quicknote.data.repository

import com.example.quicknote.data.entity.Note

interface NoteRepository {
    suspend fun getAll(): List<Note>
    suspend fun insert(note: Note)
    suspend fun update(note: Note)
    suspend fun delete(note: Note)
}