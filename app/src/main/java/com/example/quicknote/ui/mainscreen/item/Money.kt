package com.example.quicknote.ui.mainscreen.item

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.quicknote.R
import com.example.quicknote.data.entity.Note
import com.example.quicknote.data.entity.NoteContent
import com.example.quicknote.ui.theme.QuickNoteTheme
import com.example.quicknote.util.formatNumberWithComma

@Composable
fun NoteContentMoney(
    modifier: Modifier = Modifier,
    amount: Long,
) {
    val showString = formatNumberWithComma(amount)
    Text(
        modifier = modifier,
        text = "$showString ${stringResource(R.string.money_UNIT)}",
        style = MaterialTheme.typography.bodyLarge
    )
}


@Preview
@Composable
private fun MoneyPreview() {
    QuickNoteTheme {
        NoteItem(
            note = Note(
                title = "Money",
                contents = listOf(
                    NoteContent.Money(1000),
                    NoteContent.Money(1000000),
                    NoteContent.Money(1000000000)
                )
            )
        )
    }
}
