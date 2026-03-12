package com.simovic.meapp.feature.base.util

import timber.log.Timber
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Long.toLocalDateString(): String {
    val instant = Instant.ofEpochMilli(this)
    val zone = ZoneId.systemDefault()
    val local = LocalDateTime.ofInstant(instant, zone).toLocalDate()
    return local.toString() // 2025-06-12
}

fun Long.toLocalDate(): LocalDate? =
    Instant
        .ofEpochMilli(this)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()

private val birthdayFormatter = DateTimeFormatter.ofPattern("dd.MM")

fun LocalDate.toBirthDay(): String = this.format(birthdayFormatter)

const val DATE_PLACEHOLDER_YEAR = 2000

private val rssFeedInputFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
private val rssFeedOutputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

fun String.formatRssDate(): String =
    try {
        val parsed = LocalDateTime.parse(this.trim(), rssFeedInputFormatter)
        parsed.format(rssFeedOutputFormatter)
    } catch (e: Exception) {
        Timber.e(e, "Failed to parse RSS date: $this")
        this
    }
