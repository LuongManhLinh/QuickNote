package com.example.quicknote.ui.mainscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.quicknote.R
import com.example.quicknote.data.entity.Key
import com.example.quicknote.ui.custom.CustomTextField
import com.example.quicknote.ui.theme.QuickNoteTheme
import java.util.Locale


@Composable
internal fun KeyCombinationText(
    modifier: Modifier = Modifier,
    key: Key.KeyText
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        border = BorderStroke(
            dimensionResource(R.dimen.micro),
            MaterialTheme.colorScheme.tertiary
        ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.tiny_small))
    ) {
        Text(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.tiny)),
            text = key.text.ifEmpty { "  " },
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
internal fun KeyCombinationSymbol(
    modifier: Modifier = Modifier,
    key: Key.KeySymbol
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        border = BorderStroke(
            dimensionResource(R.dimen.micro),
            MaterialTheme.colorScheme.tertiary
        ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.tiny_small))
    ) {
        Icon(
            modifier = Modifier
                .size(dimensionResource(R.dimen.key_sym_size))
                .padding(dimensionResource(R.dimen.tiny)),
            painter = painterResource(key.iconId),
            contentDescription = key.contentDescription
        )
    }
}

@Composable
internal fun KeyCombinationTextEdit(
    modifier: Modifier = Modifier,
    text: String,
    onTextChange: (String) -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        border = BorderStroke(
            dimensionResource(R.dimen.micro),
            MaterialTheme.colorScheme.tertiary
        ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.tiny_small))
    ) {
        CustomTextField(
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .padding(dimensionResource(R.dimen.tiny)),
            value = text,
            onValueChange = onTextChange,
            placeholder = "  ",
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun KeyCombinationTextEditPreview() {
    QuickNoteTheme {
        KeyCombinationTextEdit(
            text = "A",
            onTextChange = { }
        )
    }
}