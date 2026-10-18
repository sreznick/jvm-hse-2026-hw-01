package org.products

sealed class ExtraValueRule {

    data class NumberRule(val displayName: String, val mustBePositive: Boolean = false) : ExtraValueRule()

    data object AnyStringRule : ExtraValueRule()

    data class AllowedValuesRule(val allowedValues: Set<String>) : ExtraValueRule()
}

