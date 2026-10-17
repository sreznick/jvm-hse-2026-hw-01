package org.example.exporter

import org.example.model.*
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JsonExporterTest {

    private val exporter = JsonExporter()

    private val sampleOil = OilProduct(1, "Mobil 1", 1125.0, 4.54, VehicleType.PASSENGER, 4.0)
    private val sampleTire = TireProduct(3, "Michelin", 1350.0, 0.3, VehicleType.PASSENGER, 16.0, Season.SUMMER)

    @Test
    fun `export produces valid json array structure`() {
        val output = exporter.export(listOf(sampleOil, sampleTire))

        assertTrue(output.trimStart().startsWith("["))
        assertTrue(output.trimEnd().endsWith("]"))
        assertTrue(output.contains("},"), "Между объектами должна быть запятая")
        assertFalse(output.contains(",\n]"), "Перед закрывающей скобкой не должно быть запятой")
    }

    @Test
    fun `export contains all fields`() {
        val output = exporter.export(listOf(sampleOil, sampleTire))

        assertTrue(output.contains("\"id\": 1"))
        assertTrue(output.contains("\"name\": \"Mobil 1\""))
        assertTrue(output.contains("\"type\": \"oil\""))
        assertTrue(output.contains("\"volume\": 4.0"))
        assertTrue(output.contains("\"diameter\": 16.0"))
        assertTrue(output.contains("\"season\": \"SUMMER\""))
    }

    @Test
    fun `null fields are rendered as json null`() {
        val products = listOf(
            AccessoryProduct(5, "Щетки", 100.0, 0.1, VehicleType.PASSENGER, null, null)
        )
        val output = exporter.export(products)

        assertTrue(output.contains("\"material\": null"))
        assertTrue(output.contains("\"color\": null"))
    }

    @Test
    fun `escapes double quotes`() {
        val products = listOf(
            OilProduct(1, "Он сказал \"привет\"", 100.0, 1.0, VehicleType.PASSENGER, 4.0)
        )
        val output = exporter.export(products)

        assertTrue(output.contains("\\\"привет\\\""))
    }
}