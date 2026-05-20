package com.qiuye.calendarkotlin.domain

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

private val manualYearMonthFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMM")
private val storageDateRegex = Regex("""\d{4}-\d{2}-\d{2}""")
private val manualYearMonthRegex = Regex("""\d{6}""")
private val basicIsoDateRegex = Regex("""\d{8}""")

fun parseStorageDateOrNull(value: String?): LocalDate? {
    val normalizedValue = value?.trim().orEmpty()
    if (normalizedValue.isEmpty()) return null
    return runCatching { parseStorageDate(normalizedValue) }.getOrNull()
}

fun parseManualDateInput(input: String): LocalDate? {
    val normalizedInput = input.trim()
    if (normalizedInput.isEmpty()) return null

    return when {
        storageDateRegex.matches(normalizedInput) -> parseStorageDateOrNull(normalizedInput)
        manualYearMonthRegex.matches(normalizedInput) ->
            runCatching { YearMonth.parse(normalizedInput, manualYearMonthFormatter).atDay(1) }.getOrNull()
        basicIsoDateRegex.matches(normalizedInput) ->
            runCatching { LocalDate.parse(normalizedInput, DateTimeFormatter.BASIC_ISO_DATE) }.getOrNull()
        else -> null
    }
}


