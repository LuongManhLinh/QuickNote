package com.example.quicknote.ui.mainscreen.item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.quicknote.R
import com.example.quicknote.data.entity.Key
import com.example.quicknote.data.entity.Note
import com.example.quicknote.data.entity.NoteContent
import com.example.quicknote.ui.theme.QuickNoteTheme


@Composable
fun NoteContentKeyCombination(
    modifier: Modifier = Modifier,
    combination: List<Key>
) {
    LazyRow(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (combination.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.empty_key_comb),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            items(combination.size) { idx ->
                when (val key = combination[idx]) {
                    is Key.KeyText -> {
                        KeyCombinationText(key = key)
                    }

                    is Key.KeySymbol -> {
                        KeyCombinationSymbol(key = key)
                    }
                }

                if (idx != combination.size - 1) {
                    Text(
                        modifier = Modifier.padding(dimensionResource(R.dimen.tiny)),
                        text = "+",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun KeyCombinationText(
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
fun KeyCombinationSymbol(
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


@Preview
@Composable
private fun KeyCombinationPreview() {
    QuickNoteTheme {
        NoteItem(
            note = Note(
                title = "KeyCombination",
                contents = listOf(
                    NoteContent.KeyCombination(
                        listOf(
                            Key.KeyText.CTRL,
                            Key.KeyText.SHIFT,
                            Key.KeyText("A"),
                        )
                    ),
                    NoteContent.KeyCombination(
                        listOf(
                            Key.KeyText("H"),
                            Key.KeySymbol.ARROW_UP,
                            Key.KeySymbol.ARROW_DOWN,
                            Key.KeySymbol.ARROW_LEFT,
                            Key.KeySymbol.ARROW_RIGHT,
                            Key.KeySymbol.WINDOW
                        )
                    )
                )
            )
        )
    }
}