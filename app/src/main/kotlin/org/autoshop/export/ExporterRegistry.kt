package org.autoshop.export

class ExporterRegistry {
    private val exporters = mutableMapOf<String, Exporter>()

    fun register(format: String, exporter: Exporter) {
        exporters[format.lowercase()] = exporter
    }

    fun getExporter(format: String): Exporter {
        return exporters[format.lowercase()]
            ?: throw IllegalArgumentException("Неизвестный формат: $format")
    }
}