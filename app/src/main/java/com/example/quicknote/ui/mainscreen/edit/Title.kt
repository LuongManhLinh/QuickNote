package com.example.quicknote.ui.mainscreen.edit

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.quicknote.R
import com.example.quicknote.ui.custom.CustomTextField

@Composable
fun NoteTitleEdit(
    modifier: Modifier = Modifier,
    title: String,
    onTitleChanged: (String) -> Unit
) {
    CustomTextField(
        modifier = modifier,
        value = title,
        onValueChange = onTitleChanged,
        textStyle = MaterialTheme.typography.titleLarge,
        placeholder = stringResource(R.string.title_placeholder)
    )

}