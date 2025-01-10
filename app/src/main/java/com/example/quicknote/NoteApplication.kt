package com.example.quicknote

import android.app.Application
import com.example.quicknote.data.container.NoteContainer
import com.example.quicknote.data.container.RoomNoteContainer

class NoteApplication: Application() {
    lateinit var noteContainer: NoteContainer
    override fun onCreate() {
        super.onCreate()
        noteContainer = RoomNoteContainer(this)
    }
}