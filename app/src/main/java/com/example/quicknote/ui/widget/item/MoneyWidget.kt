package com.example.quicknote.ui.widget.item

import androidx.compose.runtime.Composable
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.text.Text
import com.example.quicknote.R
import com.example.quicknote.util.formatNumberWithComma

@Composable
fun NoteContentMoneyWidget(
    modifier: GlanceModifier = GlanceModifier,
    amount: Long,
) {
    val showString = formatNumberWithComma(amount)
    val context = LocalContext.current
    Text(
        modifier = modifier,
        text = "$showString ${context.getString(R.string.money_UNIT)}",
    )
}