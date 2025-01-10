package com.example.quicknote.data.entity

import androidx.annotation.DrawableRes
import com.example.quicknote.R
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
sealed class NoteContent {
    @Serializable
    data class Text(
        val text: String,
    ) : NoteContent()

    @Serializable
    data class Money(
        var amount: ULong
    ) : NoteContent()

    @Serializable
    data class Datetime(
        @Serializable(with = LocalDateSerializer::class)
        val datetime: LocalDate,
    ) : NoteContent()

    @Serializable
    data class Link(
        val url: String,
    ) : NoteContent()

    @Serializable
    data class KeyCombination(
        val combination: List<Key>
    ) : NoteContent()
}





