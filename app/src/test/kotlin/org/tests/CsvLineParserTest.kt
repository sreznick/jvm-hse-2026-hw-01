package org.tests

import org.processor.CsvLineParser
import kotlin.test.Test
import kotlin.test.assertEquals

class CsvLineParserTest {

    private val parser = CsvLineParser()

    @Test
    fun `should parse simple line without quotes and check all fields`() {
        val line = "1,Mobil 1,oil,12.5$,10kg,PASSENGER,volume=4"
        val columns = parser.parseLine(line)

        assertEquals(7, columns.size, "Количество колонок должно быть ровно 7")
        assertEquals("1", columns[0], "Неверный id")
        assertEquals("Mobil 1", columns[1], "Неверное имя")
        assertEquals("oil", columns[2], "Неверный тип")
        assertEquals("12.5$", columns[3], "Неверная цена")
        assertEquals("10kg", columns[4], "Неверный вес")
        assertEquals("PASSENGER", columns[5], "Неверный транспорт")
        assertEquals("volume=4", columns[6], "Неверные доп. характеристики")
    }

    @Test
    fun `should parse line with quotes, commas inside and check all fields`() {
        val line = """3,"Шина ""Зима"", шипованная",tire,15$,2kg,PASSENGER,diameter=17;season=WINTER"""
        val columns = parser.parseLine(line)

        assertEquals(7, columns.size, "Количество колонок должно быть ровно 7")
        assertEquals("3", columns[0], "Неверный id")
        assertEquals("""Шина "Зима", шипованная""", columns[1], "Неверное имя с экранированными кавычками")
        assertEquals("tire", columns[2], "Неверный тип")
        assertEquals("15$", columns[3], "Неверная цена")
        assertEquals("2kg", columns[4], "Неверный вес")
        assertEquals("PASSENGER", columns[5], "Неверный транспорт")
        assertEquals("diameter=17;season=WINTER", columns[6], "Неверные доп. характеристики")
    }
}