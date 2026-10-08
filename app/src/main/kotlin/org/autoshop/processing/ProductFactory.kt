package org.autoshop.processing

import org.autoshop.domain.*
import org.autoshop.parser.RawRecord

class ProductFactory(
    private val normalizer: Normalizer,
    private val validator: Validator
) {
    fun createProduct(record: RawRecord): Result<Product> = runCatching {
        val id = record.id.toIntOrNull()
            ?: throw ParseException("Некорректный ID '${record.id}'")

        val price = normalizer.normalizePrice(record.price)
        val weight = normalizer.normalizeWeight(record.weight)

        validator.validateCommon(record.name, price, weight)

        val vehicle = try {
            VehicleType.valueOf(record.vehicle.uppercase())
        } catch (e: IllegalArgumentException) {
            throw ParseException("Неизвестный тип автомобиля '${record.vehicle}'")
        }

        val extraMap = parseExtra(record.extra)

        when (record.type.lowercase()) {
            "oil" -> {
                val volume = extraMap["volume"]?.toDoubleOrNull()
                    ?: throw ParseException("Объем '${extraMap["volume"]}' не является числом")
                Oil(id, record.name, price, weight, vehicle, volume)
            }
            "tire" -> {
                val diameter = extraMap["diameter"]?.toDoubleOrNull()
                    ?: throw ParseException("Объем '${extraMap["volume"]}' не является числом")
                val season = try {
                    Season.valueOf(extraMap["season"]?.uppercase() ?: "")
                } catch (e: IllegalArgumentException) {
                    throw ParseException("Объем '${extraMap["volume"]}' не является числом")
                }
                Tire(id, record.name, price, weight, vehicle, diameter, season)
            }
            "accessory" -> {
                val material = extraMap["material"]
                    ?: throw ValidationException("Отсутствует материал")
                val color = extraMap["color"]
                    ?: throw ValidationException("Отсутствует цвет")

                validator.validateAccessoryColor(color)
                Accessory(id, record.name, price, weight, vehicle, material, color)
            }
            else -> throw ParseException("Неизвестный тип товара '${record.type}'")
        }
    }

    private fun parseExtra(extra: String): Map<String, String> {
        if (extra.isBlank()) return emptyMap()
        return extra.split(";")
            .map { it.trim().split("=", limit = 2) }
            .filter { it.size == 2 }
            .associate { it[0] to it[1] }
    }
}