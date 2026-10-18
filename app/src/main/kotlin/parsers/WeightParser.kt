package parsers

import validators.ProductParseException

fun parseWeight(weight: String): Double {
    return when {
        weight.endsWith("lb") -> {
            parseWeightNum(weight.removeSuffix("lb"), weight) * 0.454
        }
        weight.endsWith("kg") -> {
            parseWeightNum(weight.removeSuffix("kg"), weight)
        }
        else -> {
            throw ProductParseException(
                "Неизвестный формат веса: '$weight'"
            )
        }
    }
}

fun parseWeightNum(weight: String, original: String): Double {
    return try {
       weight.toDouble()
    } catch(e: NumberFormatException) {
        throw ProductParseException(
            "Не удалось распарсить вес '$original'"
        )
    }
}