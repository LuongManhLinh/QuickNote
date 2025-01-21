package com.example.quicknote.ui.mainscreen.edit

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.example.quicknote.R
import com.example.quicknote.ui.custom.CustomTextField

@Composable
fun NoteContentLinkEdit(
    modifier: Modifier = Modifier,
    link: String,
    onLinkChanged: (String) -> Unit
) {
    CustomTextField(
        modifier = modifier,
        value = link,
        onValueChange = onLinkChanged,
        placeholder = stringResource(R.string.link_placeholder),
        textStyle = TextStyle(
            textDecoration = TextDecoration.Underline,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontWeight = FontWeight.Normal
        ),
        textColor = if (isSystemInDarkTheme()) {
            colorResource(R.color.link_on_dark)
        } else {
            colorResource(R.color.link_on_light)
        }
    )
}