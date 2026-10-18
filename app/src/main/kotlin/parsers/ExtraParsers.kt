package parsers

import structures.Color
import structures.Season
import validators.ProductParseException
import validators.ProductValidationException


fun parseExtra(extra: String) : Map<String, String> {
    if (extra.isBlank()) return emptyMap()

    return extra.split(";")
        .mapNotNull { part ->
            val sepInd = part.indexOf("=")

            if (sepInd == -1) {
                null
            }
            else {
                val key = part.substring(0, sepInd)
                val value = part.substring(sepInd + 1)

                key to value
            }
        }.toMap()
}

fun parseExtraOilVolume(extra: Map<String, String>) : Double? {
    val volume = extra["volume"] ?: return null
    return try {
        volume.toDouble()
    } catch (e : NumberFormatException) {
        throw ProductParseException("Объём '$volume' не является числом")
    }
}


fun parseExtraTireDiameter(extra: Map<String, String>) : Double? {
    val diameter = extra["diameter"] ?: return null
    return try {
        diameter.toDouble()
    } catch (e : NumberFormatException) {
        throw ProductParseException("Диаметр '$diameter' не является числом")
    }
}

fun parseExtraTireSeason(extra: Map<String, String>) : Season? {
    val season = extra["season"] ?: return null
    return when (season) {
        "WINTER" -> Season.WINTER
        "SUMMER" -> Season.SUMMER
        "ALL_SEASON" -> Season.ALL_SEASON
        else ->  throw ProductValidationException(
            "Недопустимое значение 'season=$season'"
        )
    }
}


fun parseExtraAccessoryMaterial(extra: Map<String, String>) : String? {
    return extra["material"]
}

fun parseExtraAccessoryColor(extra: Map<String, String>) : Color? {
    val color = extra["color"] ?: return null
    return when (color) {
        "белый" -> Color.WHITE
        "черный" -> Color.BLACK
        "серебристый" -> Color.SILVER
        else ->  throw ProductValidationException(
            "Недопустимое значение 'color=$color'"
        )
    }
}