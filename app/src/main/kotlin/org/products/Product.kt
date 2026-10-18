package org.products

data class Product(
    val id: Int,
    val name: String,
    val type: ProductType,
    val priceRub: Double,
    val weightKg: Double,
    val vehicle: VehicleType,
    val extra: Map<String, Any>
)