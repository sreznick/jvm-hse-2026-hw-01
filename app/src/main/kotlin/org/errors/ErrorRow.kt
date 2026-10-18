package org.errors

data class ErrorRow(
    val lineNumber: Int,
    val type: ErrorType,
    val message: String
) {
    fun format(): String = "[ОШИБКА] Строка $lineNumber: $message"
}