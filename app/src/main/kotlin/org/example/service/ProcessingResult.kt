package org.example.service

import org.example.model.ErrorType
import org.example.model.ParseError
import org.example.model.Product

data class ProcessingResult(
    val totalLines: Int,
    val products: List<Product>,
    val errors: List<ParseError>,
) {
    val parsingErrorsCount: Int get() = errors.count {error -> error.type == ErrorType.PARSING }
    val validationErrorsCount: Int get() = errors.count {error -> error.type == ErrorType.VALIDATION }
}