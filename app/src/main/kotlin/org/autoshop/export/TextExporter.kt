package org.autoshop.export

import org.autoshop.domain.Product
import java.io.File

class TextExporter : Exporter {
    override fun export(products: List<Product>, outputPath: String) {
        val file = File(outputPath)
        file.bufferedWriter().use { writer ->
            products.forEach { product ->
                writer.write("ID: ${product.id} | Name: ${product.name} | Price: ${product.price} | Weight: ${product.weight}\n")
            }
        }
    }
}