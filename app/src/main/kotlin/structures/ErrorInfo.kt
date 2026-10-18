package structures

import kotlinx.serialization.Serializable


@Serializable
data class ErrorInfo(
    val type: ErrorType,
    val rowNumber: Int,
    val message: String
)