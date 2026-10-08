package org.autoshop.export

import org.autoshop.domain.Product
import java.io.File

class XmlExporter : Exporter {
    override fun export(products: List<Product>, outputPath: String) {
        val xml = buildString {
            appendLine("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
            appendLine("<products>")
            products.forEach { product ->
                appendLine("  <product id=\"${product.id}\">")
                appendLine("    <name><![CDATA[${product.name}]]></name>")
                appendLine("    <price>${product.price}</price>")
                appendLine("    <weight>${product.weight}</weight>")
                appendLine("    <vehicle>${product.vehicle}</vehicle>")
                appendLine("  </product>")
            }
            appendLine("</products>")
        }
        File(outputPath).writeText(xml)
    }
}