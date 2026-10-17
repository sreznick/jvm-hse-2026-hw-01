package org.example.parser

import org.example.model.ParseError
import org.example.model.Product

sealed class RowResult {
    data class Success(val product: Product) : RowResult()
    data class Failure(val error: ParseError) : RowResult()
}