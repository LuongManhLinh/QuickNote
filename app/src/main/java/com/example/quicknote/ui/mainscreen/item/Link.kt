package com.example.quicknote.ui.mainscreen.item

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import com.example.quicknote.R
import com.example.quicknote.data.entity.Note
import com.example.quicknote.data.entity.NoteContent
import com.example.quicknote.ui.theme.QuickNoteTheme

@Composable
fun NoteContentLink(
    modifier: Modifier = Modifier,
    link: String,
) {
    val isLinkEmpty = link.isEmpty()
    val context = LocalContext.current

    val unableToOpenLink = stringResource(R.string.unable_to_open_link)
    val copiedToClipboard = stringResource(R.string.copied_to_clipboard)

    val clipboardManager = LocalClipboardManager.current

    var isPopupVisible by remember { mutableStateOf(false) }

    var offset by remember { mutableStateOf(IntOffset.Zero) }

    SelectionContainer {
        Text(
            modifier = modifier
                .pointerInput(Unit) {
                    detectTapGestures {
                        offset = IntOffset(it.x.toInt(), it.y.toInt())
                        isPopupVisible = true
                    }
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

        if (isPopupVisible) {
            NoteContentLinkPopup(
                offset = offset,
                onOpenLinkClick = {
                    if (link.isNotEmpty()) {
                        try {
                            val validLink = if (link.startsWith("http://")
                                || link.startsWith("https://"))
                            {
                                link
                            } else {
                                "https://$link"
                            }
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(validLink))
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            Toast.makeText(
                                context, unableToOpenLink, Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                onCopyClick = {
                    clipboardManager.setText(AnnotatedString(link))
                    Toast.makeText(
                        context, copiedToClipboard, Toast.LENGTH_SHORT
                    ).show()
                    isPopupVisible = false
                },
                onDismissRequest = { isPopupVisible = false },
            )
        }
    }

}

@Composable
fun NoteContentLinkPopup(
    offset: IntOffset,
    onOpenLinkClick: () -> Unit,
    onCopyClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Popup(
        offset = offset,
        onDismissRequest = onDismissRequest
    ) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = dimensionResource(R.dimen.small)
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(R.dimen.small)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.clickable { onOpenLinkClick() },
                    text = stringResource(R.string.open_link),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.padding(vertical = dimensionResource(R.dimen.very_tiny)))
                Text(
                    modifier = Modifier.clickable { onCopyClick() },
                    text = stringResource(R.string.copy),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
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

@Preview
@Composable
private fun PopupLinkPreview() {
    QuickNoteTheme {
        NoteContentLinkPopup(
            offset = IntOffset.Zero,
            onDismissRequest = {},
            onOpenLinkClick = {},
            onCopyClick = {}
        )
    }
}