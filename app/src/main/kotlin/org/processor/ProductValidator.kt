package org.processor

import org.products.ProductType
import org.products.VehicleType
import org.errors.ParsingException
import org.errors.ValidationException
import org.products.ExtraValueRule
import org.products.Product

class ProductValidator(
    private val normalizer: ValueNormalizer = ValueNormalizer()
) {
    fun validateAndBuildFields(
        rawId: String,
        rawName: String,
        rawType: String,
        rawPrice: String,
        rawWeight: String,
        rawVehicle: String,
        rawExtra: String
    ): Product {

        val id = rawId.trim().toIntOrNull()
            ?: throw ParsingException("Некорректный ID '$rawId'")

        val name = rawName.trim()
        if (name.isEmpty()) {
            throw ValidationException("Пустое название товара")
        }

        val productType = ProductType.fromCsvName(rawType.trim())
            ?: throw ParsingException("Неизвестный тип товара '$rawType'")

        val priceRub = normalizer.parsePriceToRub(rawPrice)
        val weightKg = normalizer.parseWeightToKg(rawWeight)

        if (priceRub <= 0) {
            throw ValidationException("Цена не может быть отрицательной")
        }
        if (weightKg <= 0) {
            throw ValidationException("Вес должен быть положительным")
        }
        val vehicle = VehicleType.entries.find { it.name.equals(rawVehicle, ignoreCase = true) }
            ?: throw ParsingException("Неизвестный тип автомобиля '$rawVehicle'")

        val extra = validateExtra(rawExtra, productType)

        return Product(
            id = id,
            name = name,
            type = productType,
            priceRub = priceRub,
            weightKg = weightKg,
            vehicle = vehicle,
            extra = extra
        )
    }

    private fun validateExtra(rawExtra: String, productType: ProductType): Map<String, Any> {
        if (rawExtra.isBlank()) return emptyMap()

        val result = mutableMapOf<String, Any>()
        val pairs = rawExtra.split(";")

        for (pair in pairs) {
            if (pair.isBlank()) continue

            val parts = pair.split("=", limit = 2)
            if (parts.size != 2) {
                throw ParsingException("Некорректный формат характеристики '$pair'")
            }

            val key = parts[0].trim()
            val value = parts[1].trim()
            val rule = productType.allowedExtra[key] ?: continue

            when (rule) {
                is ExtraValueRule.NumberRule -> {
                    val number = value.toDoubleOrNull()
                        ?: throw ParsingException("${rule.displayName} '$value' не является числом")
                    if (number <= 0 && rule.mustBePositive) {
                        throw ValidationException("${rule.displayName} должен быть положительным")
                    }
                    result[key] = number
                }
                is ExtraValueRule.AnyStringRule -> {
                    if (value.isEmpty()) {
                        throw ValidationException("Пустое значение для ключа '$key'")
                    }
                    result[key] = value
                }
                is ExtraValueRule.AllowedValuesRule -> {
                    if (value !in rule.allowedValues) {
                        val allowedList = rule.allowedValues.joinToString(", ")
                        throw ValidationException("Недопустимое значение $key=$value (допустимы только $allowedList)")
                    }
                    result[key] = value
                }
            }
        }
        return result
    }
}