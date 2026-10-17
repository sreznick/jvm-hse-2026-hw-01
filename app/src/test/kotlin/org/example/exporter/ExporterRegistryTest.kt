package org.example.exporter

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ExporterRegistryTest {
    @Test
    fun `returns null for unknown format`() {
        val registry = ExporterRegistry()
        registry.register(JsonExporter())

        assertNull(registry.get("yaml"))
    }

    @Test
    fun `returns null on empty registry`() {
        val registry = ExporterRegistry()

        assertNull(registry.get("json"))
    }

    @Test
    fun `ignores case in format name`() {
        val registry = ExporterRegistry()
        registry.register(JsonExporter())

        assertNotNull(registry.get("JSON"))
        assertNotNull(registry.get("Json"))
        assertNotNull(registry.get("json"))
    }

    @Test
    fun `returns all registered formats`() {
        val registry = ExporterRegistry()
        registry.register(JsonExporter())
        registry.register(XmlExporter())
        registry.register(TxtExporter())

        val formats = registry.availableFormats()

        assertEquals(setOf("json", "xml", "txt"), formats)
    }

}