package org.autoshop.processing

class Normalizer {
    fun normalizePrice(rawPrice: String): Double {
        val priceStr = rawPrice.trim()
        // Добавлен знак минуса в регулярное выражение
        val value = priceStr.replace(Regex("[^0-9.-]"), "").toDoubleOrNull()
            ?: throw ParseException("Не удалось распарсить цену '$rawPrice'")

        return when {
            priceStr.startsWith("$") || priceStr.endsWith("$") -> value * 90.0
            priceStr.endsWith("руб") -> value
            else -> throw ParseException("Неизвестная валюта '$rawPrice'")
        }
    }

    fun normalizeWeight(rawWeight: String): Double {
        val weightStr = rawWeight.trim().lowercase()
        // Добавлен знак минуса в регулярное выражение
        val value = weightStr.replace(Regex("[^0-9.-]"), "").toDoubleOrNull()
            ?: throw ParseException("Не удалось распарсить вес '$rawWeight'")

        return when {
            weightStr.endsWith("kg") -> value
            weightStr.endsWith("lb") -> value * 0.454
            else -> throw ParseException("Неизвестная единица измерения '$rawWeight'")
        }
    }
}