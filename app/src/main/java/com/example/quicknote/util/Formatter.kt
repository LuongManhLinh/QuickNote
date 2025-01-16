package com.example.quicknote.util

import java.text.DecimalFormat

fun formatNumberWithComma(number: ULong): String {
    val formatter = DecimalFormat("#,###")
    return formatter.format(number.toLong())
}

fun formatNumberWithComma(number: Int): String {
    val formatter = DecimalFormat("#,###")
    return formatter.format(number)
}