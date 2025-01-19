package com.example.quicknote.ui.mainscreen.item

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.quicknote.data.entity.Note
import com.example.quicknote.data.entity.NoteContent
import com.example.quicknote.ui.theme.QuickNoteTheme

@Composable
fun NoteContentText(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Justify
    )
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