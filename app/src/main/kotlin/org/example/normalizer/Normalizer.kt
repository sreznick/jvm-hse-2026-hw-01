package org.example.normalizer

object Normalizer {

    const val DOLLAR_RATE = 90.0
    const val POUND_RATE = 0.454

    fun normalizePrice(raw: String): Double? {
        if (raw.startsWith("$")) {
            val value = parseNumber(raw.substring(1))
            return if (value != null) {
                value * DOLLAR_RATE
            } else{
                null
            }
        }
        if (raw.endsWith("$")) {
            val value = parseNumber(raw.dropLast(1))
            return if (value != null) {
                value * DOLLAR_RATE
            } else{
                null
            }
        }
        if (raw.endsWith("руб")) {
            return parseNumber(raw.dropLast(3))
        }
        if (raw.startsWith("руб")) {
            return parseNumber(raw.substring(3))
        }
        return null
    }


    fun normalizeWeight(raw: String): Double? {
        if (raw.endsWith("kg")) {
            return parseNumber(raw.dropLast(2))
        }
        if (raw.endsWith("lb")) {
            val value = parseNumber(raw.dropLast(2))
            return if (value != null) {
                value * POUND_RATE
            } else{
                null
            }
        }
        return null
    }

    private fun parseNumber(s: String): Double? {
        if (s.isEmpty()) return null

        var i = 0
        if (s[0] == '-') {
            i = 1
            if (s.length == 1) return null
        }

        var hasDigit = false
        var hasDot = false
        while (i < s.length) {
            val c = s[i]
            when (c) {
                in '0'..'9' -> hasDigit = true
                '.' -> {
                    if (hasDot) return null
                    hasDot = true
                }
                else -> return null
            }
            i++
        }

        if (!hasDigit) return null
        return s.toDoubleOrNull()
    }
}