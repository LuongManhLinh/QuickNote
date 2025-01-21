package com.example.quicknote.ui.mainscreen.item

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.example.quicknote.R

@Composable
fun NoteItemLayout(
    modifier: Modifier = Modifier,
    @DrawableRes iconId: Int,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(dimensionResource(R.dimen.normal_medium)),
            painter = painterResource(iconId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.padding(horizontal = dimensionResource(R.dimen.small)))
        content()
    }
}