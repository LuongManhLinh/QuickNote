package com.example.quicknote.ui.widget.item

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Row
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.quicknote.R
import com.example.quicknote.data.entity.Key


@Composable
fun NoteContentKeyCombinationWidget(
    modifier: GlanceModifier = GlanceModifier,
    combination: List<Key>
) {
    val context = LocalContext.current
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (combination.isEmpty()) {
            Text(
                text = context.getString(R.string.empty_key_comb)
            )
        } else {
            combination.forEachIndexed { idx, key ->
                when (key) {
                    is Key.KeyText -> {
                        KeyCombinationTextWidget(key = key)
                    }

                    is Key.KeySymbol -> {
                        KeyCombinationSymbolWidget(key = key)
                    }
                }

                if (idx != combination.size - 1) {
                    Text(
                        modifier = GlanceModifier.padding(4.dp),
                        text = "+"
                    )

                }
            }
        }
    }
}

@Composable
private fun KeyCombinationTextWidget(
    modifier: GlanceModifier = GlanceModifier,
    key: Key.KeyText
) {
    Box(
        modifier = modifier
            .cornerRadius(8.dp)
            .background(GlanceTheme.colors.tertiaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = GlanceModifier.padding(4.dp),
            text = key.text.ifEmpty { "  " },
            style = TextStyle(
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun KeyCombinationSymbolWidget(
    modifier: GlanceModifier = GlanceModifier,
    key: Key.KeySymbol
) {
    Box(
        modifier = modifier
            .cornerRadius(8.dp)
            .background(GlanceTheme.colors.tertiaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = GlanceModifier
                .size(30.dp)
                .padding(4.dp),
            provider = ImageProvider(key.iconId),
            contentDescription = key.contentDescription
        )
    }
}