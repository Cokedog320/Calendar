package com.qiuye.calendarkotlin.domain

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DateParsingTest {

    @Test
    fun parseManualDateInputSupportsIsoDateFormat() {
        assertEquals(LocalDate.of(2026, 6, 15), parseManualDateInput("2026-06-15"))
    }

    @Test
    fun parseManualDateInputSupportsYearMonthFormat() {
        assertEquals(LocalDate.of(2026, 6, 1), parseManualDateInput("202606"))
    }

    @Test
    fun parseManualDateInputSupportsBasicIsoDateFormat() {
        assertEquals(LocalDate.of(2026, 6, 15), parseManualDateInput("20260615"))
    }

    @Test
    fun parseManualDateInputReturnsNullForInvalidInput() {
        assertNull(parseManualDateInput("2026-06"))
        assertNull(parseManualDateInput("2026-06-15x"))
        assertNull(parseManualDateInput("2026/06/15"))
        assertNull(parseManualDateInput("202613"))
        assertNull(parseManualDateInput("not-a-date"))
        assertNull(parseManualDateInput("20260230"))
    }

    @Test
    fun parseManualDateInputReturnsNullForEmptyInput() {
        assertNull(parseManualDateInput(""))
        assertNull(parseManualDateInput("   "))
    }

    @Test
    fun parseStorageDateOrNullReturnsNullForBlankOrInvalidValue() {
        assertNull(parseStorageDateOrNull(""))
        assertNull(parseStorageDateOrNull("   "))
        assertNull(parseStorageDateOrNull("2026-02-30"))
    }
}


