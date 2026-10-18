package structures

import kotlinx.serialization.Serializable


@Serializable
sealed interface Product {
    val id: Int
    val name: String
    val priceRub: Double
    val weightKg: Double
    val vehicle: Vehicle
}

@Serializable
data class Oil(
    override val id: Int,
    override val name: String,
    override val priceRub: Double,
    override val weightKg: Double,
    override val vehicle: Vehicle,
    val volume: Double?
) : Product

@Serializable
data class Tire(
    override val id: Int,
    override val name: String,
    override val priceRub: Double,
    override val weightKg: Double,
    override val vehicle: Vehicle,
    val diameter: Double?,
    val season: Season?
) : Product

@Serializable
data class Accessory(
    override val id: Int,
    override val name: String,
    override val priceRub: Double,
    override val weightKg: Double,
    override val vehicle: Vehicle,
    val material: String?,
    val color: Color?
) : Product