package org.tests

import org.processor.ValueNormalizer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ValueNormalizerTest {

    private val normalizer = ValueNormalizer()

    @Test
    fun `should convert currency correctly`() {
        val priceUsd = normalizer.parsePriceToRub("$10")
        assertEquals(900.0, priceUsd)

        val priceUsd2 = normalizer.parsePriceToRub("10$")
        assertEquals(900.0, priceUsd2)

        val priceRub = normalizer.parsePriceToRub("500руб")
        assertEquals(500.0, priceRub)
    }

    @Test
    fun `should convert weight correctly`() {
        val weightKg = normalizer.parseWeightToKg("5kg")
        assertEquals(5.0, weightKg)

        val weightLb = normalizer.parseWeightToKg("10lb")
        assertEquals(4.54, weightLb, 0.001)
    }

    @Test
    fun `should throw exception on invalid price format`() {
        assertFailsWith<org.errors.ParsingException> {
            normalizer.parsePriceToRub("abc$")
        }
    }
}