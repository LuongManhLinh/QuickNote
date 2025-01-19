package com.example.quicknote.ui.mainscreen.edit

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.quicknote.R
import com.example.quicknote.ui.custom.CustomTextField

@Composable
fun NoteContentTextEdit(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit
) {
    CustomTextField(
        modifier = modifier,
        value = text,
        onValueChange = onTextChanged,
        textStyle = MaterialTheme.typography.bodyLarge,
        placeholder = stringResource(R.string.text_placeholder)
    )
}