package com.example.quicknote.ui.mainscreen.item

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import com.example.quicknote.R
import com.example.quicknote.data.entity.Note
import com.example.quicknote.data.entity.NoteContent
import com.example.quicknote.ui.theme.QuickNoteTheme

@Composable
fun NoteContentText(
    modifier: Modifier = Modifier,
    text: String
) {
    val context = LocalContext.current
    val copiedToClipboard = stringResource(R.string.copied_to_clipboard)
    val clipboardManager = LocalClipboardManager.current

    var offset by remember { mutableStateOf(IntOffset.Zero) }
    var isPopupVisible by remember { mutableStateOf(false) }

    Box {
        Text(
            modifier = modifier
                .pointerInput(Unit) {
                    detectTapGestures {
                        offset = IntOffset(it.x.toInt(), it.y.toInt())
                        isPopupVisible = true
                    }
                },
            text = text.ifEmpty { stringResource(R.string.empty_text) },
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify
        )

        if (isPopupVisible) {
            NoteContentTextPopup(
                offset = offset,
                onDismissRequest = { isPopupVisible = false },
                onCopyClick = {
                    clipboardManager.setText(AnnotatedString(text))
                    Toast.makeText(
                        context, copiedToClipboard, Toast.LENGTH_SHORT
                    ).show()
                    isPopupVisible = false
                }
            )
        }
    }
}

@Composable
fun NoteContentTextPopup(
    offset: IntOffset,
    onDismissRequest: () -> Unit,
    onCopyClick: () -> Unit,
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
            Text(
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.small))
                    .clickable { onCopyClick() },
                text = stringResource(R.string.copy),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
private fun TextPreview() {
    QuickNoteTheme {
        NoteItem(
            note = Note(
                title = "Text",
                contents = listOf(
                    NoteContent.Text("Hello"),
                    NoteContent.Text("Khi đến Ả Rập, anh ký hợp đồng trị giá 100 triệu euro mỗi năm. Một phần tiền lương của anh được đầu tư để mua một câu lạc bộ hạng ba ở Bỉ. Ngoài ra, anh còn quay lại khoác áo đội tuyển quốc gia để tham dự Euro. Hiện tại, anh là một trong những trụ cột của Al Ittihad – đội bóng đang dẫn đầu giải VĐQG Ả Rập và đã lọt vào bán kết Cúp Nhà vua Ả Rập.")
                )
            )
        )
    }
}