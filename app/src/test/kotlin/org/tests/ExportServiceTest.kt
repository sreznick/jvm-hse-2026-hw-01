package org.tests

import org.export.ExportService
import org.export.ResultExporter
import org.processor.ProcessingResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DummyExporter(override val supportedFormat: String) : ResultExporter {
    override fun export(result: ProcessingResult): String = "mock_content_$supportedFormat"
}

class ExportServiceTest {

    @Test
    fun `should select correct exporter by format string or extension`() {
        val service = ExportService(
            listOf(
                DummyExporter("json"),
                DummyExporter("xml")
            )
        )

        val result = ProcessingResult(0, emptyList(), emptyList())

        val jsonOutput = service.formatAs(result, "json")
        assertEquals("mock_content_json", jsonOutput)

        val xmlOutput = service.formatAs(result, "xml")
        assertEquals("mock_content_xml", xmlOutput)
    }

    @Test
    fun `should throw exception for unsupported format`() {
        val service = ExportService(listOf(DummyExporter("json")))
        val result = ProcessingResult(0, emptyList(), emptyList())

        assertFailsWith<IllegalArgumentException> {
            service.formatAs(result, "yaml")
        }
    }
}