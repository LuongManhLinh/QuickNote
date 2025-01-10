package com.example.quicknote.data.container

import android.content.Context
import com.example.quicknote.data.database.NoteDatabase
import com.example.quicknote.data.repository.NoteRepository
import com.example.quicknote.data.repository.RoomNoteRepository

class RoomNoteContainer(
    private val context: Context
): NoteContainer {
    override val noteRepository: NoteRepository by lazy {
        RoomNoteRepository(NoteDatabase.getDatabase(context).noteDao())
    }
}