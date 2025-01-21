package com.example.quicknote.ui.mainscreen.edit

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.example.quicknote.R
import com.example.quicknote.ui.custom.CustomMoneyTextField
import com.example.quicknote.ui.theme.QuickNoteTheme
import com.example.quicknote.util.formatNumberWithComma
import java.util.Locale
import com.example.quicknote.data.entity.Key
import com.example.quicknote.ui.mainscreen.item.KeyCombinationSymbol
import com.example.quicknote.ui.mainscreen.item.KeyCombinationText
import kotlin.math.max

@Composable
internal fun NoteItemEditMoneyDialog(
    currentMoney: Long,
    isAdding: Boolean,
    value: String,
    onValueChanged: (String) -> Unit,
    onDone: () -> Unit,
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            shape = RoundedCornerShape(dimensionResource(R.dimen.normal)),
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(R.dimen.small)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isAdding) {
                        stringResource(R.string.dialog_title_money_add)
                    } else {
                        stringResource(R.string.dialog_title_money_sub)
                    },
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(R.string.money_current) + " ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(formatNumberWithComma(currentMoney))
                        }
                        append(" " + stringResource(R.string.money_UNIT))
                    },
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(Modifier.padding(vertical = dimensionResource(R.dimen.small)))

                CustomMoneyTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = dimensionResource(R.dimen.micro),
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(dimensionResource(R.dimen.small))
                        ),
                    contentPadding = PaddingValues(dimensionResource(R.dimen.small)),
                    value = value,
                    onValueChange = onValueChanged,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = if (isAdding) {
                                Icons.Default.Add
                            } else {
                                Icons.Default.Remove
                            },
                            contentDescription = null
                        )
                    }
                )

                Spacer(Modifier.padding(vertical = dimensionResource(R.dimen.small)))

                Text(
                    text = buildAnnotatedString {
                        append(stringResource(R.string.money_after_changing) + " ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(
                                formatNumberWithComma(
                                    if (isAdding) {
                                        currentMoney + value.toLong()
                                    } else {
                                        currentMoney - value.toLong()
                                    }
                                )
                            )
                        }
                        append(" " + stringResource(R.string.money_UNIT))
                    },
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(Modifier.padding(vertical = dimensionResource(R.dimen.normal)))

                DialogBottomButtons(
                    onCancel = onDismissRequest,
                    onDone = onDone
                )

            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun NoteItemEditKeyCombinationDialog(
    keyList: List<Key>,
    onKeyListChange: (List<Key>) -> Unit,
    onDismissRequest: () -> Unit,
    onDone: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            shape = RoundedCornerShape(dimensionResource(R.dimen.normal))
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(R.dimen.small)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.dialog_title_key_comb),
                    style = MaterialTheme.typography.headlineLarge
                )

                Spacer(Modifier.padding(vertical = dimensionResource(R.dimen.small)))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .border(
                                width = dimensionResource(R.dimen.micro),
                                color = MaterialTheme.colorScheme.onSurface,
                                shape = RoundedCornerShape(dimensionResource(R.dimen.small))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        KeyCombinationEditField(
                            modifier = Modifier.padding(dimensionResource(R.dimen.small)),
                            combination = keyList,
                            onKeyListChange = onKeyListChange
                        )
                    }
                    Spacer(Modifier.padding(horizontal = dimensionResource(R.dimen.tiny)))
                    Icon(
                        modifier = Modifier.clickable {
                            onKeyListChange(keyList.toMutableList().apply { add(Key.KeyText("")) })
                        },
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                    Spacer(Modifier.padding(horizontal = dimensionResource(R.dimen.tiny)))
                    Icon(
                        modifier = Modifier.clickable {
                            onKeyListChange(keyList.toMutableList().apply { removeAt(keyList.size - 1) })
                        },
                        imageVector = Icons.Default.Remove,
                        contentDescription = null
                    )
                }

                Spacer(Modifier.padding(vertical = dimensionResource(R.dimen.small)))

                FlowRow(
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Key.KeyText.all.forEach { key ->
                        KeyCombinationText(
                            modifier = Modifier
                                .padding(dimensionResource(R.dimen.tiny_small))
                                .clickable {
                                    onKeyListChange(keyList.toMutableList().apply { add(key) })
                                },
                            key = key
                        )
                    }
                }

                FlowRow(
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Key.KeySymbol.all.forEach { key ->
                        KeyCombinationSymbol(
                            modifier = Modifier
                                .padding(dimensionResource(R.dimen.tiny_small))
                                .clickable {
                                    onKeyListChange(keyList.toMutableList().apply { add(key) })
                                },
                            key = key
                        )
                    }
                }

                Spacer(Modifier.padding(vertical = dimensionResource(R.dimen.normal)))

                DialogBottomButtons(
                    onCancel = onDismissRequest,
                    onDone = onDone
                )
            }
        }
    }
}


@Composable
private fun KeyCombinationEditField(
    modifier: Modifier = Modifier,
    combination: List<Key>,
    onKeyListChange: (List<Key>) -> Unit
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
                        KeyCombinationTextEdit(
                            text = key.text,
                            onTextChange = {
                                onKeyListChange(
                                    combination.toMutableList().apply {
                                        this[idx] = Key.KeyText(it)
                                    }
                                )
                            }
                        )
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
private fun DialogBottomButtons(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onDone: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            text = stringResource(R.string.cancel).uppercase(Locale.ROOT),
            modifier = Modifier.clickable { onCancel() }
        )

        Spacer(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.small)))

        Text(
            text = stringResource(R.string.done).uppercase(Locale.ROOT),
            modifier = Modifier.clickable { onDone() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    QuickNoteTheme {
        NoteItemEditMoneyDialog(
            currentMoney = 1000,
            value = "100000",
            isAdding = true,
            onValueChanged = {},
            onDone = {},
            onDismissRequest = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview2() {
    QuickNoteTheme {
        NoteItemEditKeyCombinationDialog(
            keyList = listOf(
                Key.KeyText.CTRL,
                Key.KeyText.ALT,
                Key.KeyText.SHIFT,
                Key.KeySymbol.WINDOW,
                Key.KeyText.CTRL,
                Key.KeyText.ALT,
                Key.KeyText.SHIFT,
                Key.KeySymbol.WINDOW
            ),
            onKeyListChange = {},
            onDismissRequest = {},
            onDone = {}
        )
    }
}