package com.example.quicknote.ui.mainscreen

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.example.quicknote.R
import com.example.quicknote.data.entity.Key
import com.example.quicknote.data.entity.Note
import com.example.quicknote.data.entity.NoteContent
import com.example.quicknote.ui.theme.QuickNoteTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
internal fun NoteItem(
    modifier: Modifier = Modifier,
    note: Note,
    isSelectingNote: Boolean = false,
    isSelected: Boolean = false,
    onSelectionChanged: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .then(
                if (isSelectingNote) {
                    Modifier.clickable {
                        onSelectionChanged(!isSelected)
                    }
                } else {
                    Modifier
                }
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(dimensionResource(R.dimen.small)),
                verticalArrangement = Arrangement.Center,
            ) {
                NoteTitle(
                    title = note.title.ifEmpty {
                        stringResource(R.string.untitled)
                    }
                )
                note.contents.forEach { content ->
                    HorizontalDivider(Modifier.padding(vertical = dimensionResource(R.dimen.small)))

                    when (content) {
                        is NoteContent.Text -> {
                            NoteContentText(
                                text = content.text.ifEmpty {
                                    stringResource(R.string.empty_text)
                                }
                            )
                        }

                        is NoteContent.Datetime -> {
                            NoteContentDatetime(
                                datetime = content.datetime
                            )
                        }

                        is NoteContent.KeyCombination -> {
                            NoteContentKeyCombination(
                                combination = content.combination
                            )
                        }

                        is NoteContent.Link -> {
                            NoteContentLink(
                                link = content.url,
                                onLinkClicked = { link ->
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
                                            context, "Không thể mở liên kết", Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            )
                        }

                        is NoteContent.Money -> {
                            NoteContentMoney(
                                amount = content.amount,
                            )
                        }
                    }
                }
            }

            if (isSelectingNote) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = onSelectionChanged
                )
            }
        }

    }
}


@Composable
private fun NoteTitle(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        modifier = modifier,
        text = title,
        style = MaterialTheme.typography.titleLarge
    )
}




@Composable
private fun NoteContentText(
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


@Composable
private fun NoteContentDatetime(
    modifier: Modifier = Modifier,
    datetime: LocalDate
) {
    var showSpecial by rememberSaveable { mutableStateOf(false) }

    val today = LocalDate.now()
    val dateToShow = if (showSpecial) {
        datetime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    } else {
        when (datetime) {
            today -> {
                stringResource(R.string.today)
            }
            today.minusDays(1) -> {
                stringResource(R.string.yesterday)
            }
            today.plusDays(1) -> {
                stringResource(R.string.tomorrow)
            }
            in today.plusDays(2)..today.plusDays(7) -> {
                stringResource(R.string.n_day_after, today.until(datetime).days)
            }
            in today.minusDays(7)..today.minusDays(2) -> {
                stringResource(R.string.n_day_before, datetime.until(today).days)
            }
            else -> {
                datetime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
            }
        }
    }

    Text(
        modifier = modifier.clickable {
            showSpecial = !showSpecial
        },
        text = dateToShow,
        style = MaterialTheme.typography.bodyLarge
    )
}


@Composable
private fun NoteContentKeyCombination(
    modifier: Modifier = Modifier,
    combination: List<Key>
) {
    val keyAsAny: List<Any> = combination.flatMap { listOf(it, "+") }.dropLast(1)

    LazyRow(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(keyAsAny) { key ->
            when (key) {
                is Key.KeyText -> {
                    Card(
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
                            text = key.text,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                is Key.KeySymbol -> {
                    Card(
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

                is String -> {
                    Text(
                        modifier = Modifier.padding(dimensionResource(R.dimen.tiny)),
                        text = key,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}


@Composable
private fun NoteContentLink(
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



@Composable
private fun NoteContentMoney(
    modifier: Modifier = Modifier,
    amount: ULong,
) {
    var unit by rememberSaveable { mutableStateOf(MoneyUnit.K) }

    val amountString: String
    val unitString: String
    when (unit) {
        MoneyUnit.UNIT -> {
            amountString = amount.toString()
            unitString = stringResource(R.string.money_UNIT)
        }
        MoneyUnit.K -> {
            amountString = if ((amount % 1000u).toUInt() == 0u) {
                (amount / 1000u).toString()
            } else {
                String
                    .format(Locale.getDefault(), "%.2f", amount.toFloat() / 1000)
            }
            unitString = stringResource(R.string.money_K)
        }
        MoneyUnit.M -> {
            amountString = if ((amount % 1000000u).toUInt() == 0u) {
                (amount / 1000000u).toString()
            } else {
                String
                    .format(Locale.getDefault(), "%.2f", amount.toFloat() / 1000000)
            }
            unitString = stringResource(R.string.money_M)
        }
    }

    Text(
        modifier = modifier.clickable {
            unit = when (unit) {
                MoneyUnit.UNIT -> MoneyUnit.K
                MoneyUnit.K -> MoneyUnit.M
                MoneyUnit.M -> MoneyUnit.UNIT
            }
        },
        text = "$amountString $unitString",
        style = MaterialTheme.typography.bodyLarge
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

@Preview
@Composable
private fun DatetimePreview() {
    QuickNoteTheme {
        NoteItem(
            note = Note(
                title = "Datetime",
                contents = listOf(
                    NoteContent.Datetime(LocalDate.now()),
                    NoteContent.Datetime(LocalDate.now().plusDays(1)),
                    NoteContent.Datetime(LocalDate.now().minusDays(1)),
                    NoteContent.Datetime(LocalDate.now().plusDays(2)),
                    NoteContent.Datetime(LocalDate.now().minusDays(2)),
                    NoteContent.Datetime(LocalDate.now().plusDays(7)),
                    NoteContent.Datetime(LocalDate.now().minusDays(7))
                )
            )
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
private fun MoneyPreview() {
    QuickNoteTheme {
        NoteItem(
            note = Note(
                title = "Money",
                contents = listOf(
                    NoteContent.Money(1000u),
                    NoteContent.Money(1000000u),
                    NoteContent.Money(1000000000u)
                )
            )
        )
    }
}



