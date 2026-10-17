package org.example.validation

import org.example.model.Color
import org.example.model.Season

object Validator {

    fun validateName(name: String): String?{
        return if (name.isEmpty()) "Пустое название товара" else null
    }

    fun validatePrice(price: Double): String?{
        return if (price < 0) "Цена не может быть отрицательной" else null
    }

    fun validateWeight(weight: Double): String?{
        return if (weight < 0) "Вес не может быть отрицательным" else null
    }

    fun validateColor(raw: String): Color?{
        return Color.fromRu(raw)
    }

    fun validateSeason(raw: String): Season?{
        return Season.entries.firstOrNull { it.name == raw.uppercase() }
    }
}