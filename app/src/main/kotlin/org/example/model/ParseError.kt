package org.example.model

enum class ErrorType { PARSING, VALIDATION }

data class ParseError(
    val lineNumber: Int,
    val message: String,
    val type: ErrorType,
)