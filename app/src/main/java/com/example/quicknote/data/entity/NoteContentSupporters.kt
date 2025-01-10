package com.example.quicknote.data.entity

import androidx.annotation.DrawableRes
import com.example.quicknote.R
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object LocalDateSerializer : KSerializer<LocalDate> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString(), formatter)
    }
}

sealed interface Key {
    @Serializable
    data class KeyText(val text: String) : Key {
        override fun toString() = text

        companion object {
            val CTRL = KeyText("Ctrl")
            val ALT = KeyText("Alt")
            val SHIFT = KeyText("Shift")
            val TAB = KeyText("Tab")
            val ENTER = KeyText("Enter")
            val ESC = KeyText("Esc")
            val BACKSPACE = KeyText("Backspace")
            val DELETE = KeyText("Delete")
            val FN = KeyText("Fn")
            val SPACE = KeyText("Dấu cách")
        }
    }

    @Serializable
    data class KeySymbol(
        @DrawableRes val iconId: Int,
        val contentDescription: String,
    ) : Key {
        companion object {
            val WINDOW = KeySymbol(R.drawable.key_sym_windows, "Windows")
            val ARROW_UP = KeySymbol(R.drawable.key_sym_arrow_up, "Arrow Up")
            val ARROW_DOWN = KeySymbol(R.drawable.key_sym_arrow_down, "Arrow Down")
            val ARROW_LEFT = KeySymbol(R.drawable.key_sym_arrow_left, "Arrow Left")
            val ARROW_RIGHT = KeySymbol(R.drawable.key_sym_arrow_right, "Arrow Right")
        }
    }

}


data class NoteContentPresentation(
    @DrawableRes val iconId: Int,
    val text: String
) {
    companion object {
        val allTypes = listOf(
            NoteContentPresentation(R.drawable.note_type_text, "Ghi chú thường"),
            NoteContentPresentation(R.drawable.note_type_money, "Tiền"),
            NoteContentPresentation(R.drawable.note_type_datetime, "Ngày tháng"),
            NoteContentPresentation(R.drawable.note_type_link, "Liên kết"),
            NoteContentPresentation(R.drawable.note_type_key_comb, "Phím tắt")
        )
    }
}
