package com.example.quicknote.ui.mainscreen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.example.quicknote.R
import com.example.quicknote.ui.theme.QuickNoteTheme
import com.example.quicknote.util.formatNumberWithComma

@Composable
internal fun NoteItemEditMoneyDialog(
    title: String,
    currentMoney: ULong,
    isAdding: Boolean,
    value: String,
    onValueChanged: (String) -> Unit,
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
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Số tiền hiện tại: ${formatNumberWithComma(currentMoney)} Đồng",
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(Modifier.padding(vertical = dimensionResource(R.dimen.small)))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = value,
                    onValueChange = onValueChanged,
                    placeholder = { Text("Nhập số tiền") },
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
                    },
                )

                Spacer(Modifier.padding(vertical = dimensionResource(R.dimen.small)))

                Text(
                    text = "Số tiền sau khi thay đổi: ${
                        if (isAdding) {
                            formatNumberWithComma(currentMoney + value.toULong())
                        } else {
                            formatNumberWithComma(currentMoney - value.toULong())
                        }
                    } Đồng",
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(Modifier.padding(vertical = dimensionResource(R.dimen.small)))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(stringResource(R.string.cancel))

                    Spacer(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.small)))

                    Text(stringResource(R.string.done))
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
            title = "Cộng thêm tiền",
            currentMoney = 1000u,
            value = "1",
            isAdding = true,
            onValueChanged = {},
            onDismissRequest = {}
        )
    }
}