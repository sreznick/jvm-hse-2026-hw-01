package org.example.exporter

import org.example.model.*
import kotlin.test.Test
import kotlin.test.assertTrue

class TxtExporterTest {

    private val exporter = TxtExporter()

    private val sampleProducts = listOf(
        OilProduct(1, "Mobil 1", 1125.0, 4.54, VehicleType.PASSENGER, 4.0),
        TireProduct(3, "Michelin", 1350.0, 0.3, VehicleType.PASSENGER, 16.0, Season.SUMMER),
        AccessoryProduct(5, "Щетки", 100.0, 0.1, VehicleType.PASSENGER, "резина", Color.BLACK),
    )

    @Test
    fun `export produces correct report structure`() {
        val output = exporter.export(sampleProducts)

        assertTrue(output.contains("=== Отчёт о товарах автомагазина ==="))
        assertTrue(output.contains("Всего товаров: 3"))
        assertTrue(output.contains("Масло"), "Должен быть русский перевод типа")
        assertTrue(output.contains("Шина"))
        assertTrue(output.contains("Аксессуар"))
    }

    @Test
    fun `export contains specific fields for each type`() {
        val output = exporter.export(sampleProducts)
        assertTrue(output.contains("объём 4.0 л"))
        assertTrue(output.contains("диаметр 16.0"))
        assertTrue(output.contains("сезон SUMMER"))
        assertTrue(output.contains("материал резина"))
    }

    @Test
    fun `null fields are replaced with dashes`() {
        val products = listOf(
            AccessoryProduct(5, "Щетки", 100.0, 0.1, VehicleType.PASSENGER, null, null)
        )
        val output = exporter.export(products)

        assertTrue(output.contains("материал -"))
        assertTrue(output.contains("цвет -"))
    }
}