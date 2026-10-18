package org.export

import org.processor.ProcessingResult
import java.util.Locale

class JsonExporter : ResultExporter {
    override val supportedFormat: String = "json"

    override fun export(result: ProcessingResult): String = buildString {
        appendLine("[")
        result.products.forEachIndexed { index, p ->
            appendLine("  {")
            appendLine("    \"id\": ${p.id},")
            appendLine("    \"name\": \"${escapeJson(p.name)}\",")
            appendLine("    \"type\": \"${p.type.csvName}\",")
            appendLine("    \"priceRub\": ${String.format(Locale.US, "%.2f", p.priceRub)},")
            appendLine("    \"weightKg\": ${String.format(Locale.US, "%.3f", p.weightKg)},")
            appendLine("    \"vehicle\": \"${p.vehicle.name}\",")

            if (p.extra.isEmpty()) {
                appendLine("    \"extra\": {}")
            } else {
                appendLine("    \"extra\": {")
                val entries = p.extra.entries.toList()
                entries.forEachIndexed { extraIndex, (key, value) ->
                    val formattedValue = when (value) {
                        is Number -> value.toString()
                        else -> "\"${escapeJson(value.toString())}\""
                    }
                    val comma = if (extraIndex < entries.lastIndex) "," else ""
                    appendLine("      \"${escapeJson(key)}\": $formattedValue$comma")
                }
                appendLine("    }")
            }

            val blockComma = if (index < result.products.lastIndex) "," else ""
            appendLine("  }$blockComma")
        }
        append("]")
    }

    private fun escapeJson(text: String): String {
        return text.replace("\\", "\\\\").replace("\"", "\\\"")
    }
}