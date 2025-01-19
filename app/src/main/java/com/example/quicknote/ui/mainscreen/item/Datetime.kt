package com.example.quicknote.ui.mainscreen.item

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.quicknote.R
import com.example.quicknote.data.entity.Note
import com.example.quicknote.data.entity.NoteContent
import com.example.quicknote.ui.theme.QuickNoteTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun NoteContentDatetime(
    modifier: Modifier = Modifier,
    datetime: LocalDate
) {
    var showSpecial by rememberSaveable { mutableStateOf(false) }

    val today = LocalDate.now()
    val dateToShow = if (showSpecial) {
        datetime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    } else {
        when (datetime) {
            today -> {
                stringResource(R.string.today)
            }
            today.minusDays(1) -> {
                stringResource(R.string.yesterday)
            }
            today.plusDays(1) -> {
                stringResource(R.string.tomorrow)
            }
            in today.plusDays(2)..today.plusDays(7) -> {
                stringResource(R.string.n_day_after, today.until(datetime).days)
            }
            in today.minusDays(7)..today.minusDays(2) -> {
                stringResource(R.string.n_day_before, datetime.until(today).days)
            }
            else -> {
                datetime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            }
        }
    }

    Text(
        modifier = modifier.clickable {
            showSpecial = !showSpecial
        },
        text = dateToShow,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Preview
@Composable
private fun DatetimePreview() {
    QuickNoteTheme {
        NoteItem(
            note = Note(
                title = "Datetime",
                contents = listOf(
                    NoteContent.Datetime(LocalDate.now()),
                    NoteContent.Datetime(LocalDate.now().plusDays(1)),
                    NoteContent.Datetime(LocalDate.now().minusDays(1)),
                    NoteContent.Datetime(LocalDate.now().plusDays(2)),
                    NoteContent.Datetime(LocalDate.now().minusDays(2)),
                    NoteContent.Datetime(LocalDate.now().plusDays(7)),
                    NoteContent.Datetime(LocalDate.now().minusDays(7))
                )
            )
        )
    }
}