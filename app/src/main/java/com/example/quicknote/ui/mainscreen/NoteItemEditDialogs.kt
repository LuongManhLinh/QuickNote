package com.example.quicknote.ui.mainscreen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.example.quicknote.R
import com.example.quicknote.ui.custom.CustomMoneyTextField
import com.example.quicknote.ui.theme.QuickNoteTheme
import com.example.quicknote.util.formatNumberWithComma
import java.util.Locale

@Composable
internal fun NoteItemEditMoneyDialog(
    currentMoney: ULong,
    isAdding: Boolean,
    value: String,
    onValueChanged: (String) -> Unit,
    onDone: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            shape = RoundedCornerShape(dimensionResource(R.dimen.small)),
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(R.dimen.small)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isAdding) {
                        stringResource(R.string.money_add)
                    } else {
                        stringResource(R.string.money_sub)
                    },
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(R.string.money_current))
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(formatNumberWithComma(currentMoney))
                        }
                        append(" " + stringResource(R.string.money_UNIT))
                    },
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(Modifier.padding(vertical = dimensionResource(R.dimen.small)))

                CustomMoneyTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = dimensionResource(R.dimen.micro),
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(dimensionResource(R.dimen.small))
                        ),
                    contentPadding = PaddingValues(dimensionResource(R.dimen.small)),
                    value = value,
                    onValueChanged = onValueChanged,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = if (isAdding) {
                                Icons.Default.Add
                            } else {
                                Icons.Default.Remove
                            },
                            contentDescription = null
                        )
                    }
                )

                Spacer(Modifier.padding(vertical = dimensionResource(R.dimen.small)))

                Text(
                    text = buildAnnotatedString {
                        append(stringResource(R.string.money_after_changing))
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(
                                formatNumberWithComma(
                                    if (isAdding) {
                                        currentMoney + value.toULong()
                                    } else {
                                        currentMoney - value.toULong()
                                    }
                                )
                            )
                        }
                        append(" " + stringResource(R.string.money_UNIT))
                    },
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(Modifier.padding(vertical = dimensionResource(R.dimen.small)))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = stringResource(R.string.cancel).uppercase(Locale.ROOT),
                        modifier = Modifier.clickable { onDismissRequest() }
                    )

                    Spacer(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.small)))

                    Text(
                        text = stringResource(R.string.done).uppercase(Locale.ROOT),
                        modifier = Modifier.clickable { onDone() }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    QuickNoteTheme {
        NoteItemEditMoneyDialog(
            currentMoney = 1000u,
            value = "100000",
            isAdding = true,
            onValueChanged = {},
            onDone = {},
            onDismissRequest = {}
        )
    }
}