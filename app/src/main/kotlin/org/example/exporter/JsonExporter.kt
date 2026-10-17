package org.example.exporter

import org.example.model.AccessoryProduct
import org.example.model.OilProduct
import org.example.model.Product
import org.example.model.TireProduct

class JsonExporter : Exporter {

    override val format: String = "json"

    override fun export(products: List<Product>): String {
        val sb = StringBuilder()
        sb.appendLine("[")
        products.forEachIndexed { index, product ->
            sb.append("  ").append(productToJson(product))
            if (index < products.size - 1) sb.append(",")
            sb.appendLine()
        }
        sb.append("]")
        return sb.toString()
    }

    private fun productToJson(p: Product): String {
        val fields = mutableListOf<String>()
        fields += "\"id\": ${p.id}"
        fields += "\"name\": \"${escapeJson(p.name)}\""
        fields += "\"type\": \"${productTypeOf(p)}\""
        fields += "\"priceRub\": ${p.priceRub}"
        fields += "\"weightKg\": ${p.weightKg}"
        fields += "\"vehicle\": \"${p.vehicle}\""

        when (p) {
            is OilProduct -> fields += "\"volume\": ${p.volume}"
            is TireProduct -> {
                fields += "\"diameter\": ${p.diameter}"
                fields += "\"season\": \"${p.season}\""
            }
            is AccessoryProduct -> {
                fields += "\"material\": ${p.material?.let { "\"${escapeJson(it)}\"" } ?: "null"}"
                fields += "\"color\": ${p.color?.let { "\"${it.ru}\"" } ?: "null"}"
            }
        }

        return "{ ${fields.joinToString(", ")} }"
    }

    private fun escapeJson(s: String): String =
        s.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
}