package com.example.quicknote.ui.mainscreen.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.core.text.isDigitsOnly
import com.example.quicknote.R
import com.example.quicknote.ui.custom.CustomMoneyTextField


@Composable
fun NoteContentMoneyEdit(
    modifier: Modifier = Modifier,
    amount: Long,
    onMoneyChanged: (Long) -> Unit,
) {

    var openDialog by rememberSaveable { mutableStateOf(false) }
    var isAdding by rememberSaveable { mutableStateOf(true) }
    var changingValue by rememberSaveable { mutableLongStateOf(0L) }


    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomMoneyTextField(
            modifier = Modifier,
            value = amount.toString(),
            onValueChange = {
                val value = if (it.isEmpty() || !it.isDigitsOnly()) {
                    0L
                } else {
                    var parsed = it.toLongOrNull()
                    while (parsed == null) {
                        parsed = it.dropLast(1).toLongOrNull()
                    }
                    parsed
                }

                onMoneyChanged(value)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )

        Text(
            text = " " + stringResource(R.string.money_UNIT),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.weight(1f))
        Icon(
            modifier = Modifier.clickable {
                openDialog = true
                isAdding = true
            },
            imageVector = Icons.Default.Add,
            contentDescription = null
        )
        Spacer(Modifier.padding(horizontal = dimensionResource(R.dimen.tiny)))
        Icon(
            modifier = Modifier.clickable {
                openDialog = true
                isAdding = false
            },
            imageVector = Icons.Default.Remove,
            contentDescription = null
        )
        Spacer(Modifier.padding(horizontal = dimensionResource(R.dimen.tiny)))
    }

    if (openDialog) {
        NoteItemEditMoneyDialog(
            currentMoney = amount,
            value = changingValue.toString(),
            onValueChanged = {
                val value = if (it.isEmpty() || !it.isDigitsOnly()) {
                    0L
                } else {
                    var parsed = it.toLongOrNull()
                    while (parsed == null) {
                        parsed = it.dropLast(1).toLongOrNull()
                    }
                    parsed
                }
                changingValue = value!!
            },
            isAdding = isAdding,
            onDone = {
                onMoneyChanged(
                    if (isAdding) {
                        amount + changingValue
                    } else {
                        amount - changingValue
                    }
                )
                openDialog = false
            },
            onDismissRequest = { openDialog = false }
        )
    }
}
