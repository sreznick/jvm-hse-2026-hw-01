package org.processor

import org.errors.ErrorRow
import org.errors.ErrorType
import org.products.Product

data class ProcessingResult(
    val totalRowsProcessed: Int,
    val products: List<Product>,
    val errors: List<ErrorRow>
) {
    val loadedProductsCount: Int
        get() = products.size

    val parsingErrorsCount: Int
        get() = errors.count { it.type == ErrorType.PARSING }

    val validationErrorsCount: Int
        get() = errors.count { it.type == ErrorType.VALIDATION }
}