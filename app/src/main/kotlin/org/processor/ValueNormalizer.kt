package org.processor

import org.errors.ParsingException

class ValueNormalizer {

    companion object {
        private const val USD_TO_RUB_RATE = 90.0
        private const val LB_TO_KG_RATE = 0.454
    }

    fun parsePriceToRub(rawPrice: String): Double {
        val trimmed = rawPrice.trim()

        val (numberStr, multiplier) = when {
            trimmed.startsWith("$") -> trimmed.removePrefix("$").trim() to USD_TO_RUB_RATE
            trimmed.endsWith("$") -> trimmed.removeSuffix("$").trim() to USD_TO_RUB_RATE
            trimmed.endsWith("руб") -> trimmed.removeSuffix("руб").trim() to 1.0
            else -> throw ParsingException("Не удалось распарсить цену '$rawPrice'")
        }

        val amount = numberStr.toDoubleOrNull()
            ?: throw ParsingException("Не удалось распарсить цену '$rawPrice'")

        return amount * multiplier
    }

    fun parseWeightToKg(rawWeight: String): Double {
        val trimmed = rawWeight.trim()

        val (numberStr, multiplier) = when {
            trimmed.endsWith("kg") -> trimmed.removeSuffix("kg").trim() to 1.0
            trimmed.endsWith("lb") -> trimmed.removeSuffix("lb").trim() to LB_TO_KG_RATE
            else -> throw ParsingException("Не удалось распарсить вес '$rawWeight'")
        }

        val weight = numberStr.toDoubleOrNull()
            ?: throw ParsingException("Не удалось распарсить вес '$rawWeight'")

        return weight * multiplier
    }
}