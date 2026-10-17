package org.example.exporter

import org.example.model.AccessoryProduct
import org.example.model.OilProduct
import org.example.model.Product
import org.example.model.TireProduct

interface Exporter {

    val format: String

    fun export(products: List<Product>): String
}

internal fun productTypeOf(product: Product): String = when (product) {
    is OilProduct       -> "oil"
    is TireProduct      -> "tire"
    is AccessoryProduct -> "accessory"
}