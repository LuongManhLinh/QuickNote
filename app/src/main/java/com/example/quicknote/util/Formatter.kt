package com.example.quicknote.util

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


fun formatNumberWithComma(number: Int): String {
    val formatter = DecimalFormat("#,###", DecimalFormatSymbols.getInstance(Locale.US))
    return formatter.format(number)
}

fun formatNumberWithComma(number: Long): String {
    val formatter = DecimalFormat("#,###", DecimalFormatSymbols.getInstance(Locale.US))
    return formatter.format(number)
}

fun formatNumberWithComma(number: ULong): String {
    return formatNumberWithComma(number.toLong())
}

fun removeCommaFromNumber(number: String): String {
    return number.replace(",", "")
}

fun formatDate(date: LocalDate): String {
    return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
}