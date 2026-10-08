package org.autoshop.domain

sealed class Product {
    abstract val id: Int
    abstract val name: String
    abstract val price: Double
    abstract val weight: Double 
    abstract val vehicle: VehicleType
}

data class Oil(
    override val id: Int,
    override val name: String,
    override val price: Double,
    override val weight: Double,
    override val vehicle: VehicleType,
    val volume: Double
) : Product()

data class Tire(
    override val id: Int,
    override val name: String,
    override val price: Double,
    override val weight: Double,
    override val vehicle: VehicleType,
    val diameter: Double,
    val season: Season
) : Product()

data class Accessory(
    override val id: Int,
    override val name: String,
    override val price: Double,
    override val weight: Double,
    override val vehicle: VehicleType,
    val material: String,
    val color: String
) : Product()