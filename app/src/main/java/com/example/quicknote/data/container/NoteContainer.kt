package com.example.quicknote.data.container

import com.example.quicknote.data.repository.NoteRepository

interface NoteContainer {
    val noteRepository: NoteRepository
}
