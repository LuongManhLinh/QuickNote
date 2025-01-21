package com.example.quicknote.ui.custom

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quicknote.R
import com.example.quicknote.ui.theme.QuickNoteTheme
import com.example.quicknote.util.formatNumberWithComma
import com.example.quicknote.util.removeCommaFromNumber

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
    placeholder: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle,
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = textStyle.copy(color = MaterialTheme.colorScheme.onBackground)
                )
            }
            innerTextField()
        },
        keyboardOptions = keyboardOptions
    )
}

@Composable
fun CustomMoneyTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground),
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Done
    ),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val showingValue = formatNumberWithComma(value.toLong())

    BasicTextField(
        modifier = modifier.width(IntrinsicSize.Min),
        value = TextFieldValue(
            text = showingValue,
            selection = TextRange(showingValue.length)
        ),
        onValueChange = {
            onValueChange(removeCommaFromNumber(it.text))
        },
        textStyle = textStyle,
        decorationBox = {
            Row(
                modifier = Modifier.padding(contentPadding),
            ) {
                if (leadingIcon != null) {
                    leadingIcon()
                    Spacer(Modifier.padding(dimensionResource(R.dimen.small)))
                }
                if (value.isEmpty()) {
                    Text(
                        text = "0",
                        style = textStyle.copy(color = MaterialTheme.colorScheme.onBackground)
                    )
                }
                it()
                if (trailingIcon != null) {
                    Spacer(Modifier.padding(dimensionResource(R.dimen.small)))
                    trailingIcon()
                }
            }
        },
        keyboardOptions = keyboardOptions
    )
}

@Preview(showBackground = true)
@Composable
private fun CustomMoneyTextFieldPreview() {
    QuickNoteTheme {
        CustomMoneyTextField(
            value = "1000",
            onValueChange = {},
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            },
            trailingIcon = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomTextFieldPreview() {
    QuickNoteTheme(darkTheme = true) {
        Card {
            CustomTextField(
                value = "1000",
                onValueChange = {}
            )
        }
    }
}
