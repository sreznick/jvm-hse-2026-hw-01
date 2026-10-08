package org.autoshop.export

import org.autoshop.domain.Product

interface Exporter {
    fun export(products: List<Product>, outputPath: String)
}