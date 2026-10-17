package org.example.model

enum class Color(val ru: String) {
    BLACK("черный"),
    WHITE("белый"),
    SILVER("серебристый");

    companion object {
        fun fromRu(value: String): Color? {
            return entries.firstOrNull { color -> color.ru.equals(value, ignoreCase = true) }
        }
    }
}