package org.example.model

sealed class Product {
    abstract val id: Int
    abstract val name: String
    abstract val priceRub: Double
    abstract val weightKg: Double
    abstract val vehicle: VehicleType
}

data class OilProduct(
    override val id: Int,
    override val name: String,
    override val priceRub: Double,
    override val weightKg: Double,
    override val vehicle: VehicleType,
    val volume: Double,
) : Product()

data class TireProduct(
    override val id: Int,
    override val name: String,
    override val priceRub: Double,
    override val weightKg: Double,
    override val vehicle: VehicleType,
    val diameter: Double,
    val season: Season,
) : Product()

data class AccessoryProduct(
    override val id: Int,
    override val name: String,
    override val priceRub: Double,
    override val weightKg: Double,
    override val vehicle: VehicleType,
    val material: String?,
    val color: Color?,
) : Product()