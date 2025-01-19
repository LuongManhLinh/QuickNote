package com.example.quicknote.ui.mainscreen.edit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.quicknote.R
import com.example.quicknote.data.entity.Key
import com.example.quicknote.ui.custom.CustomTextField
import com.example.quicknote.ui.mainscreen.item.NoteContentKeyCombination
import com.example.quicknote.ui.theme.QuickNoteTheme


@Composable
fun NoteContentKeyCombinationEdit(
    modifier: Modifier = Modifier,
    combination: List<Key>,
    onKeyListChange: (List<Key>) -> Unit
) {
    var openDialog by rememberSaveable { mutableStateOf(false) }
    var newKeyList by rememberSaveable { mutableStateOf(combination) }

    NoteContentKeyCombination(
        modifier = modifier.clickable { openDialog = true },
        combination = combination,
    )

    if (openDialog) {
        NoteItemEditKeyCombinationDialog(
            keyList = newKeyList,
            onKeyListChange = { newKeyList = it },
            onDismissRequest = { openDialog = false },
            onDone = {
                onKeyListChange(newKeyList)
                openDialog = false
            }
        )
    }
}

@Composable
fun KeyCombinationTextEdit(
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
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
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