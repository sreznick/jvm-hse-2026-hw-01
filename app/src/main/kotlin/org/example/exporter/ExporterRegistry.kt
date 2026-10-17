package org.example.exporter

class ExporterRegistry {

    private val exporters = mutableMapOf<String, Exporter>()

    fun register(exporter: Exporter) {
        exporters[exporter.format.lowercase()] = exporter
    }

    fun get(format: String): Exporter? = exporters[format.lowercase()]

    fun availableFormats(): Set<String> = exporters.keys.toSet()
}