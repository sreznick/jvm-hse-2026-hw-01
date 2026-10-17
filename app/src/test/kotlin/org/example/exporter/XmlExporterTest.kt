package org.example.exporter

import org.example.model.*
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class XmlExporterTest {

    private val exporter = XmlExporter()

    private val sampleOil = OilProduct(1, "Mobil 1", 1125.0, 4.54, VehicleType.PASSENGER, 4.0)
    private val sampleTire = TireProduct(3, "Michelin", 1350.0, 0.3, VehicleType.PASSENGER, 16.0, Season.SUMMER)

    @Test
    fun `export produces valid xml structure`() {
        val output = exporter.export(listOf(sampleOil))

        assertTrue(output.startsWith("<?xml"))
        assertTrue(output.contains("<products>"))
        assertTrue(output.contains("</products>"))
    }

    @Test
    fun `export contains correct tags for each type`() {
        val output = exporter.export(listOf(sampleOil, sampleTire))

        assertTrue(output.contains("<id>1</id>"))
        assertTrue(output.contains("<type>oil</type>"))
        assertTrue(output.contains("<volume>4.0</volume>"))
        assertTrue(output.contains("<diameter>16.0</diameter>"))
        assertTrue(output.contains("<season>SUMMER</season>"))
    }

    @Test
    fun `null fields are omitted`() {
        val products = listOf(
            AccessoryProduct(5, "Щетки", 100.0, 0.1, VehicleType.PASSENGER, null, null)
        )
        val output = exporter.export(products)

        assertFalse(output.contains("<material>"))
        assertFalse(output.contains("<color>"))
    }

    @Test
    fun `escapes special characters`() {
        val products = listOf(
            OilProduct(1, "Bosch & Air <Test>", 100.0, 1.0, VehicleType.PASSENGER, 4.0)
        )
        val output = exporter.export(products)

        assertTrue(output.contains("Bosch &amp; Air &lt;Test&gt;"))
    }
}