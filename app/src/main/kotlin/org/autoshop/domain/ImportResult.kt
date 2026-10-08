package org.autoshop.domain

enum class ErrorType {
    PARSE_ERROR,
    VALIDATION_ERROR
}

data class ErrorRecord(
    val lineNumber: Int,
    val type: ErrorType,
    val message: String
)

data class ImportStatistics(
    var totalProcessed: Int = 0,
    var successfullyLoaded: Int = 0,
    var parseErrors: Int = 0,
    var validationErrors: Int = 0
)

data class ImportResult(
    val products: List<Product>,
    val errors: List<ErrorRecord>,
    val stats: ImportStatistics
)