package org.example.exporter

import org.example.model.AccessoryProduct
import org.example.model.OilProduct
import org.example.model.Product
import org.example.model.TireProduct

class XmlExporter : Exporter {

    override val format: String = "xml"

    override fun export(products: List<Product>): String {
        val sb = StringBuilder()
        sb.appendLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
        sb.appendLine("<products>")
        products.forEach { sb.append(productToXml(it)) }
        sb.append("</products>")
        return sb.toString()
    }

    private fun productToXml(p: Product): String {
        val sb = StringBuilder()
        sb.appendLine("  <product>")
        sb.appendLine("    <id>${p.id}</id>")
        sb.appendLine("    <name>${escapeXml(p.name)}</name>")
        sb.appendLine("    <type>${productTypeOf(p)}</type>")
        sb.appendLine("    <priceRub>${p.priceRub}</priceRub>")
        sb.appendLine("    <weightKg>${p.weightKg}</weightKg>")
        sb.appendLine("    <vehicle>${p.vehicle}</vehicle>")

        when (p) {
            is OilProduct -> sb.appendLine("    <volume>${p.volume}</volume>")

            is TireProduct -> {
                sb.appendLine("    <diameter>${p.diameter}</diameter>")
                sb.appendLine("    <season>${p.season}</season>")
            }

            is AccessoryProduct -> {
                if (p.material != null) sb.appendLine("    <material>${escapeXml(p.material)}</material>")
                if (p.color != null)    sb.appendLine("    <color>${escapeXml(p.color.ru)}</color>")
            }
        }

        sb.appendLine("  </product>")
        return sb.toString()
    }

    private fun escapeXml(s: String): String =
        s.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;")
}