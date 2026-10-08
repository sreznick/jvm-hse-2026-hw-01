package org.autoshop.processing

class Validator {
    fun validateCommon(name: String, price: Double, weight: Double) {
        if (name.isBlank()) throw ValidationException("Пустое название товара")
        if (price < 0) throw ValidationException("Цена не может быть отрицательной")
        if (weight < 0) throw ValidationException("Вес не может быть отрицательным")
    }

    fun validateAccessoryColor(color: String) {
        val validColors = setOf("черный", "белый", "серебристый")
        if (color.lowercase() !in validColors) {
            throw ValidationException("Недопустимое значение color=$color")
        }
    }
}