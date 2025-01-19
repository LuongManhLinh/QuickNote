package com.example.quicknote.ui.mainscreen.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.example.quicknote.R
import com.example.quicknote.data.entity.Note
import com.example.quicknote.data.entity.NoteContent
import com.example.quicknote.ui.theme.QuickNoteTheme

@Composable
fun NoteContentLink(
    modifier: Modifier = Modifier,
    link: String,
    onLinkClicked: (String) -> Unit
) {
    val isLinkEmpty = link.isEmpty()

    Text(
        modifier = modifier
            .clickable {
                onLinkClicked(link)
            },
        text = if (isLinkEmpty) {
            stringResource(R.string.empty_link)
        } else {
            link
        },
        style = MaterialTheme.typography.bodyLarge,
        color = if (isLinkEmpty) {
            Color.Unspecified
        } else {
            if (isSystemInDarkTheme()) {
                colorResource(R.color.link_on_dark)
            } else {
                colorResource(R.color.link_on_light)
            }
        },
        textDecoration = if (isLinkEmpty) {
            TextDecoration.None
        } else {
            TextDecoration.Underline
        }
    )
}

@Preview
@Composable
private fun LinkPreview() {
    QuickNoteTheme {
        NoteItem(
            note = Note(title = "Link",
                contents = listOf(
                    NoteContent.Link("https://www.google.com"),
                    NoteContent.Link(""),
                )
            )
        )
    }
}