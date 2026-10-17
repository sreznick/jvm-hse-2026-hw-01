package org.example.validation

object ExtraKeys {

    const val VOLUME    = "volume"
    const val DIAMETER  = "diameter"
    const val SEASON    = "season"
    const val MATERIAL  = "material"
    const val COLOR     = "color"

    val ALLOWED: Map<String, Set<String>> = mapOf(
        "oil"       to setOf(VOLUME),
        "tire"      to setOf(DIAMETER, SEASON),
        "accessory" to setOf(MATERIAL, COLOR),
    )

    fun isAllowed(key: String, value: String): Boolean{
        return (ALLOWED[key]?.contains(value) == true)
    }
}