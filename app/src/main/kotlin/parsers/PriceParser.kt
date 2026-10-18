    package parsers

    import validators.ProductParseException

    fun parsePrice(price: String): Double {
        return when {
            price.startsWith("$") -> {
                parsePriceNum(price.removePrefix("$"), price) * 90
            }
            price.endsWith("$") -> {
                parsePriceNum(price.removeSuffix("$"), price) * 90
            }
            price.endsWith("руб") -> {
                parsePriceNum(price.removeSuffix("руб"), price)
            }
            else -> {
                throw ProductParseException (
                    "Не удалось распарсить цену '$price'"
                )
            }
        }
    }

    fun parsePriceNum(price: String, original: String): Double {
        return try {
            price.toDouble()
        } catch (e: NumberFormatException) {
            throw ProductParseException(
                "Не удалось распарсить цену '$original'"
            )
        }
    }