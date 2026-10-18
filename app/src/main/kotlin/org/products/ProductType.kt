package org.products

enum class ProductType(
    val csvName: String,
    val allowedExtra: Map<String, ExtraValueRule>
) {
    OIL(
        csvName = "oil",
        allowedExtra = mapOf(
            "volume" to ExtraValueRule.NumberRule("Объем", mustBePositive = true)
        )
    ),
    TIRE(
        csvName = "tire",
        allowedExtra = mapOf(
            "diameter" to ExtraValueRule.NumberRule("Диаметр", mustBePositive = true),
            "season" to ExtraValueRule.AllowedValuesRule(setOf("SUMMER", "WINTER", "ALL_SEASON"))
        )
    ),
    ACCESSORY(
        csvName = "accessory",
        allowedExtra = mapOf(
            "material" to ExtraValueRule.AnyStringRule,
            "color" to ExtraValueRule.AllowedValuesRule(setOf("черный", "белый", "серебристый"))
        )
    );

    companion object {
        fun fromCsvName(name: String): ProductType? {
            return entries.find { it.csvName.equals(name.trim(), ignoreCase = true) }
        }
    }
}