package org.example.validation

import org.example.model.Color
import org.example.model.Season
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ValidatorTest {

    // ---------- Имя ----------
    // validateName возвращает String?: null = ок, строка = ошибка

    @Test
    fun `valid name returns null`() {
        assertNull(Validator.validateName("Mobil 1"))
    }

    @Test
    fun `empty name returns error message`() {
        assertEquals("Пустое название товара", Validator.validateName(""))
    }

    // ---------- Цена ----------

    @Test
    fun `positive price returns null`() {
        assertNull(Validator.validatePrice(100.0))
    }

    @Test
    fun `zero price returns null`() {
        assertNull(Validator.validatePrice(0.0))
    }

    @Test
    fun `negative price returns error message`() {
        assertEquals("Цена не может быть отрицательной", Validator.validatePrice(-5.0))
    }

    // ---------- Вес ----------

    @Test
    fun `positive weight returns null`() {
        assertNull(Validator.validateWeight(1.5))
    }

    @Test
    fun `negative weight returns error message`() {
        assertEquals("Вес не может быть отрицательным", Validator.validateWeight(-1.0))
    }

    // ---------- Цвет ----------
    // validateColor возвращает Color?: null = ошибка, значение = ок

    @Test
    fun `valid color черный returns BLACK`() {
        assertEquals(Color.BLACK, Validator.validateColor("черный"))
    }

    @Test
    fun `valid color белый returns WHITE`() {
        assertEquals(Color.WHITE, Validator.validateColor("белый"))
    }

    @Test
    fun `valid color серебристый returns SILVER`() {
        assertEquals(Color.SILVER, Validator.validateColor("серебристый"))
    }

    @Test
    fun `valid color in different case`() {
        assertEquals(Color.BLACK, Validator.validateColor("Черный"))
    }

    @Test
    fun `invalid color red returns null`() {
        assertNull(Validator.validateColor("red"))
    }

    @Test
    fun `invalid color синий returns null`() {
        assertNull(Validator.validateColor("синий"))
    }

    // ---------- Сезон ----------
    // validateSeason возвращает Season?: null = ошибка, значение = ок

    @Test
    fun `valid season SUMMER returns SUMMER`() {
        assertEquals(Season.SUMMER, Validator.validateSeason("SUMMER"))
    }

    @Test
    fun `valid season WINTER returns WINTER`() {
        assertEquals(Season.WINTER, Validator.validateSeason("WINTER"))
    }

    @Test
    fun `valid season ALL_SEASON returns ALL_SEASON`() {
        assertEquals(Season.ALL_SEASON, Validator.validateSeason("ALL_SEASON"))
    }

    @Test
    fun `valid season in lowercase`() {
        assertEquals(Season.WINTER, Validator.validateSeason("winter"))
    }

    @Test
    fun `invalid season AUTUMN returns null`() {
        assertNull(Validator.validateSeason("AUTUMN"))
    }

    @Test
    fun `empty season returns null`() {
        assertNull(Validator.validateSeason(""))
    }
}