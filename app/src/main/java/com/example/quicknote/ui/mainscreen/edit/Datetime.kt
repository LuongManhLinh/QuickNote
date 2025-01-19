package com.example.quicknote.ui.mainscreen.edit

import androidx.compose.foundation.clickable
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.quicknote.R
import com.example.quicknote.util.formatDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun NoteContentDatetimeEdit(
    modifier: Modifier = Modifier,
    datetime: LocalDate,
    onDatetimeChanged: (LocalDate) -> Unit,
) {
    val today = LocalDate.now()
    val dateToShow = when (datetime) {
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
            formatDate(datetime)
        }
    }

    var isShowingDatePicker by remember { mutableStateOf(false) }

    Text(
        modifier = modifier.clickable { isShowingDatePicker = true },
        text = dateToShow,
        style = MaterialTheme.typography.bodyLarge
    )

    if (isShowingDatePicker) {
        DatePickerModal(
            onDateMillisSelected = { selectedDateMillis ->
                if (selectedDateMillis != null) {
                    onDatetimeChanged(LocalDate.ofEpochDay(selectedDateMillis / 86400000))
                }
            },
            onDismiss = { isShowingDatePicker = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerModal(
    onDateMillisSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateMillisSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text(stringResource(R.string.done))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}