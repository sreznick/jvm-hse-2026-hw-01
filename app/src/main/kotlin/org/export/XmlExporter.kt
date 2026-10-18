package org.export

import org.processor.ProcessingResult
import java.util.Locale

class XmlExporter : ResultExporter {
    override val supportedFormat: String = "xml"

    override fun export(result: ProcessingResult): String = buildString {
        appendLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
        appendLine("<products>")
        for (p in result.products) {
            appendLine("  <product id=\"${p.id}\">")
            appendLine("    <name>${escapeXml(p.name)}</name>")
            appendLine("    <type>${p.type.csvName}</type>")
            appendLine("    <priceRub>${String.format(Locale.US, "%.2f", p.priceRub)}</priceRub>")
            appendLine("    <weightKg>${String.format(Locale.US, "%.3f", p.weightKg)}</weightKg>")
            appendLine("    <vehicle>${p.vehicle.name}</vehicle>")
            if (p.extra.isEmpty()) {
                appendLine("    <extra/>")
            } else {
                appendLine("    <extra>")
                for ((key, value) in p.extra) {
                    appendLine("      <$key>${escapeXml(value.toString())}</$key>")
                }
                appendLine("    </extra>")
            }
            appendLine("  </product>")
        }
        append("</products>")
    }

    private fun escapeXml(text: String): String {
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
    }
}