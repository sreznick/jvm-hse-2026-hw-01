package org.tests

import org.errors.ErrorRow
import org.errors.ErrorType
import org.export.JsonExporter
import org.export.TextReportExporter
import org.export.XmlExporter
import org.processor.ProcessingResult
import org.products.Product
import org.products.ProductType
import org.products.VehicleType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ExportersTest {

    private fun createSampleResult(): ProcessingResult {
        val products = listOf(
            Product(
                id = 1,
                name = """Шина "Зима"""",
                type = ProductType.TIRE,
                priceRub = 1500.5,
                weightKg = 8.25,
                vehicle = VehicleType.PASSENGER,
                extra = linkedMapOf("diameter" to 17.0, "season" to "WINTER")
            )
        )
        val errors = listOf(
            ErrorRow(lineNumber = 2, type = ErrorType.PARSING, message = "Некорректная цена"),
            ErrorRow(lineNumber = 3, type = ErrorType.VALIDATION, message = "Пустое имя")
        )
        return ProcessingResult(
            totalRowsProcessed = 3,
            products = products,
            errors = errors
        )
    }

    private fun String.normalizeNewlines(): String = this.replace("\r\n", "\n").trim()

    @Test
    fun `JsonExporter should generate valid JSON and escape quotes`() {
        val exporter = JsonExporter()
        val actual = exporter.export(createSampleResult()).normalizeNewlines()

        val expected = """
            [
              {
                "id": 1,
                "name": "Шина \"Зима\"",
                "type": "tire",
                "priceRub": 1500.50,
                "weightKg": 8.250,
                "vehicle": "PASSENGER",
                "extra": {
                  "diameter": 17.0,
                  "season": "WINTER"
                }
              }
            ]
        """.trimIndent().normalizeNewlines()

        assertEquals(expected, actual)
    }

    @Test
    fun `JsonExporter should return empty array when no products`() {
        val emptyResult = ProcessingResult(0, emptyList(), emptyList())
        val actual = JsonExporter().export(emptyResult).normalizeNewlines()

        val expected = """
            [
            ]
        """.trimIndent().normalizeNewlines()

        assertEquals(expected, actual)
    }

    @Test
    fun `XmlExporter should generate valid XML and escape special characters`() {
        val exporter = XmlExporter()
        val actual = exporter.export(createSampleResult()).normalizeNewlines()

        val expected = """
            <?xml version="1.0" encoding="UTF-8"?>
            <products>
              <product id="1">
                <name>Шина &quot;Зима&quot;</name>
                <type>tire</type>
                <priceRub>1500.50</priceRub>
                <weightKg>8.250</weightKg>
                <vehicle>PASSENGER</vehicle>
                <extra>
                  <diameter>17.0</diameter>
                  <season>WINTER</season>
                </extra>
              </product>
            </products>
        """.trimIndent().normalizeNewlines()

        assertEquals(expected, actual)
    }

    @Test
    fun `TextReportExporter should include statistics, errors and products`() {
        val exporter = TextReportExporter()
        val actual = exporter.export(createSampleResult()).normalizeNewlines()

        assertTrue(actual.contains("Обработано строк: 3"), "Должно быть общее число строк")
        assertTrue(actual.contains("Успешно загружено товаров: 1"), "Должно быть число загруженных товаров")
        assertTrue(actual.contains("Ошибок парсинга: 1"), "Должно быть число ошибок парсинга")
        assertTrue(actual.contains("Ошибок валидации: 1"), "Должно быть число ошибок валидации")
        assertTrue(actual.contains("Некорректная цена"), "Должен быть текст ошибки парсинга")
        assertTrue(actual.contains("Пустое имя"), "Должен быть текст ошибки валидации")
        assertTrue(actual.contains("""[#1] Шина "Зима""""), "Должна быть строка с товаром")
    }
}