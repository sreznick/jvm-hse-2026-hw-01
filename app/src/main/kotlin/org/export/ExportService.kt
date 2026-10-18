package org.export

import org.processor.ProcessingResult
import java.io.File

class ExportService(
    exporters: List<ResultExporter>
) {
    private val exportersByFormat: Map<String, ResultExporter> =
        exporters.associateBy { it.supportedFormat.lowercase() }

    fun saveToFile(result: ProcessingResult, filePath: String) {
        val file = File(filePath)
        val extension = file.extension.lowercase()

        val exporter = exportersByFormat[extension]
            ?: throw IllegalArgumentException(
                "Формат '$extension' не поддерживается. Доступные форматы: ${exportersByFormat.keys.joinToString(", ")}"
            )

        val content = exporter.export(result)
        file.writeText(content, Charsets.UTF_8)
    }

    fun formatAs(result: ProcessingResult, format: String): String {
        val exporter = exportersByFormat[format.lowercase()]
            ?: throw IllegalArgumentException("Формат '$format' не поддерживается")
        return exporter.export(result)
    }

}