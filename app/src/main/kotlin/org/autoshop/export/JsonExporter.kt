package org.autoshop.export

import org.autoshop.domain.*
import java.io.File

class JsonExporter : Exporter {
    override fun export(products: List<Product>, outputPath: String) {
        val json = products.joinToString(prefix = "[\n", postfix = "\n]", separator = ",\n") { product ->
            """
            |  {
            |    "id": ${product.id},
            |    "name": "${product.name}",
            |    "price": ${product.price},
            |    "weight": ${product.weight},
            |    "vehicle": "${product.vehicle}",
            |    "type": "${getTypeName(product)}"
            |  }
            """.trimMargin()
        }
        File(outputPath).writeText(json)
    }

    private fun getTypeName(product: Product): String = when (product) {
        is Oil -> "oil"
        is Tire -> "tire"
        is Accessory -> "accessory"
    }
}