package org.example.normalizer

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class NormalizerTest {

    @Test
    fun `converts dollar price in front`() {
        val result = Normalizer.normalizePrice("$12.5")
        assertNotNull(result)
        assertEquals(1125.0, result)
    }

    @Test
    fun `converts dollar price at the end`() {
        val result = Normalizer.normalizePrice("15$")
        assertNotNull(result)
        assertEquals(1350.0, result)
    }

    @Test
    fun `converts integer dollar price`() {
        val result = Normalizer.normalizePrice("$10")
        assertNotNull(result)
        assertEquals(900.0, result)
    }

    @Test
    fun `keeps ruble price as is`() {
        val result = Normalizer.normalizePrice("300.5руб")
        assertNotNull(result)
        assertEquals(300.5, result)
    }

    @Test
    fun `keeps integer ruble price as is`() {
        val result = Normalizer.normalizePrice("100руб")
        assertNotNull(result)
        assertEquals(100.0, result)
    }

    @Test
    fun `keeps negative dollar price (validation is next layer)`() {
        val result = Normalizer.normalizePrice("-5$")
        assertNotNull(result)
        assertEquals(-450.0, result)
    }

    @Test
    fun `returns null for price without currency`() {
        assertNull(Normalizer.normalizePrice("12.5"))
    }

    @Test
    fun `returns null for non numeric price`() {
        assertNull(Normalizer.normalizePrice("abc"))
    }

    @Test
    fun `returns null for price with two dots`() {
        assertNull(Normalizer.normalizePrice("$1.2.3"))
    }

    @Test
    fun `returns null for empty dollar`() {
        assertNull(Normalizer.normalizePrice("$"))
    }

    // ---------- Вес ----------

    @Test
    fun `keeps kilograms as is`() {
        val result = Normalizer.normalizeWeight("1.2kg")
        assertNotNull(result)
        assertEquals(1.2, result)
    }

    @Test
    fun `converts pounds to kilograms`() {
        val result = Normalizer.normalizeWeight("10lb")
        assertNotNull(result)
        assertEquals(4.54, result)
    }

    @Test
    fun `converts integer pounds`() {
        val result = Normalizer.normalizeWeight("2lb")
        assertNotNull(result)
        assertEquals(0.908, result)
    }

    @Test
    fun `returns null for weight without unit`() {
        assertNull(Normalizer.normalizeWeight("10"))
    }

    @Test
    fun `returns null for non numeric weight`() {
        assertNull(Normalizer.normalizeWeight("abc"))
    }

    @Test
    fun `returns null for weight with two dots`() {
        assertNull(Normalizer.normalizeWeight("1.2.3kg"))
    }
}