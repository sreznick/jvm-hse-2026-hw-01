package exporters

class ExporterPicker {
    private val exporters: Map<String, Exporter> = mapOf(
        "json" to JsonExporter(),
        "xml" to XmlExporter(),
        "txt" to TextExporter()
    )

    fun getExporter(format: String): Exporter? {
        return exporters[format.lowercase()]
    }
}