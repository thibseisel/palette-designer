package com.github.thibseisel.ratioimageview

import org.junit.Test

import org.junit.Assert.*

class UtilsTest {

    @Test
    fun parseRatio_whenNull_isNaN() {
        val aspectRatio = parseRatio(null)
        assertTrue(aspectRatio.isNaN())
    }

    @Test
    fun parseRatio_whenEmpty_isNaN() {
        val aspectRatio = parseRatio("")
        assertTrue(aspectRatio.isNaN())
    }

    @Test
    fun parseRatio_whenValidInteger_isDecodedAsFloat() {
        val integerRatio = parseRatio("4")
        assertEquals(4.0f, integerRatio, 0f)
    }

    @Test
    fun parseRatio_whenValidFloat_isDecodedAsFloat() {
        val floatRatio = parseRatio("1.78")
        assertEquals(1.78f, floatRatio, 0f)
    }

    @Test
    fun parseRatio_whenNegative_isNaN() {
        val negativeRatio = parseRatio("-1.78f")
        assertTrue(negativeRatio.isNaN())
    }

    @Test
    fun parseRatio_whenMalformedRatio_isNaN() {
        val emptyNominatorRatio = parseRatio(":9")
        assertTrue(emptyNominatorRatio.isNaN())

        val emptyDenominatorRatio = parseRatio("16:")
        assertTrue(emptyDenominatorRatio.isNaN())
    }

    @Test
    fun parseRatio_whenValidRatio_isFractionResult() {
        val aspectRatio = parseRatio("16:9")
        assertEquals(16f / 9f, aspectRatio, 0f)
    }

    @Test
    fun parseRatio_whenDecimalFraction_isFractionResult() {
        val aspectRatio = parseRatio("1.25:0.5")
        assertEquals(1.25f / 0.5f, aspectRatio, 0f)
    }
}