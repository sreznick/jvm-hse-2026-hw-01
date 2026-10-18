package structures

import kotlinx.serialization.Serializable


@Serializable
data class Results (
    val products: List<Product>,
    val errors: List<ErrorInfo>
) {
    val parsingErrorsCount: Int
        get() = errors.count { it.type == ErrorType.PARSING }

    val validationErrorsCount: Int
        get() = errors.count { it.type == ErrorType.VALIDATION }
}