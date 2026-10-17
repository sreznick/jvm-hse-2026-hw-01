package org.example.parser

import org.example.model.*
import org.example.normalizer.Normalizer
import org.example.validation.ExtraKeys
import org.example.validation.Validator

object RowParser {

    private val KNOWN_TYPES = setOf("oil", "tire", "accessory")

    fun parse(fields: List<String>, lineNumber: Int): RowResult {
        if (fields.size < 6) {
            return parsingError(lineNumber, "Ожидалось 6–7 полей, получено ${fields.size}")
        }

        val idRaw      = fields[0]
        val name       = fields[1]
        val type       = fields[2]
        val priceRaw   = fields[3]
        val weightRaw  = fields[4]
        val vehicleRaw = fields[5]
        val extraRaw   = fields.getOrElse(6) { "" }

        val id = idRaw.toIntOrNull()
            ?: return parsingError(lineNumber, "Не удалось распарсить id '$idRaw'")

        if (type !in KNOWN_TYPES) {
            return parsingError(lineNumber, "Неизвестный тип товара '$type'")
        }

        val priceRub = Normalizer.normalizePrice(priceRaw)
            ?: return parsingError(lineNumber, "Не удалось распарсить цену '$priceRaw'")

        val weightKg = Normalizer.normalizeWeight(weightRaw)
            ?: return parsingError(lineNumber, "Не удалось распарсить вес '$weightRaw'")

        val vehicle = VehicleType.entries.firstOrNull { vehicle -> vehicle.name == vehicleRaw.uppercase() }
            ?: return parsingError(lineNumber, "Неизвестный тип автомобиля '$vehicleRaw'")

        val extras = parseExtras(extraRaw, type)

        val nameError = Validator.validateName(name)
        if (nameError != null) {
            return validationError(lineNumber, nameError)
        }

        val priceError = Validator.validatePrice(priceRub)
        if (priceError != null) {
            return validationError(lineNumber, priceError)
        }

        val weightError = Validator.validateWeight(weightKg)
        if (weightError != null) {
            return validationError(lineNumber, weightError)
        }

        return when (type) {
            "oil"       -> buildOil(id, name, priceRub, weightKg, vehicle, extras, lineNumber)
            "tire"      -> buildTire(id, name, priceRub, weightKg, vehicle, extras, lineNumber)
            "accessory" -> buildAccessory(id, name, priceRub, weightKg, vehicle, extras, lineNumber)
            else        -> error("unreachable")
        }
    }

    private fun parseExtras(raw: String, type: String): Map<String, String> {
        if (raw.isEmpty()) return emptyMap()
        return raw.split(';')
            .mapNotNull { pair ->
                val idx = pair.indexOf('=')
                if (idx <= 0) return@mapNotNull null
                val key = pair.substring(0, idx)
                val value = pair.substring(idx + 1)
                if (!ExtraKeys.isAllowed(type, key)) return@mapNotNull null
                key to value
            }
            .toMap()
    }

    private fun buildOil(
        id: Int, name: String, price: Double, weight: Double,
        vehicle: VehicleType, extras: Map<String, String>, lineNumber: Int,
    ): RowResult {
        val volumeRaw = extras[ExtraKeys.VOLUME]
            ?: return parsingError(lineNumber, "Отсутствует обязательный параметр 'volume'")

        val volume = volumeRaw.toDoubleOrNull()
            ?: return parsingError(lineNumber, "Объем '$volumeRaw' не является числом")

        return RowResult.Success(
            OilProduct(id, name, price, weight, vehicle, volume)
        )
    }

    private fun buildTire(
        id: Int, name: String, price: Double, weight: Double,
        vehicle: VehicleType, extras: Map<String, String>, lineNumber: Int,
    ): RowResult {
        val diameterRaw = extras[ExtraKeys.DIAMETER]
            ?: return parsingError(lineNumber, "Отсутствует обязательный параметр 'diameter'")

        val diameter = diameterRaw.toDoubleOrNull()
            ?: return parsingError(lineNumber, "Диаметр '$diameterRaw' не является числом")

        val seasonRaw = extras[ExtraKeys.SEASON]
            ?: return parsingError(lineNumber, "Отсутствует обязательный параметр 'season'")

        val season = Validator.validateSeason(seasonRaw)
            ?: return validationError(lineNumber, "Недопустимое значение 'season=$seasonRaw'")

        return RowResult.Success(
            TireProduct(id, name, price, weight, vehicle, diameter, season)
        )
    }

    private fun buildAccessory(
        id: Int, name: String, price: Double, weight: Double,
        vehicle: VehicleType, extras: Map<String, String>, lineNumber: Int,
    ): RowResult {
        val material = extras[ExtraKeys.MATERIAL]

        val colorRaw = extras[ExtraKeys.COLOR]
        val color = if (colorRaw != null) {
            Validator.validateColor(colorRaw)
                ?: return validationError(lineNumber, "Недопустимое значение 'color=$colorRaw'")
        } else null

        return RowResult.Success(
            AccessoryProduct(id, name, price, weight, vehicle, material, color)
        )
    }

    private fun parsingError(lineNumber: Int, message: String) =
        RowResult.Failure(ParseError(lineNumber, message, ErrorType.PARSING))

    private fun validationError(lineNumber: Int, message: String) =
        RowResult.Failure(ParseError(lineNumber, message, ErrorType.VALIDATION))
}