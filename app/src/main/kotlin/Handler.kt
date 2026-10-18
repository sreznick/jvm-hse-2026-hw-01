import parsers.parseProduct
import structures.RawProductInfo
import structures.Results
import structures.Product
import structures.ErrorInfo
import structures.ErrorType
import validators.ProductParseException
import validators.ProductValidationException
import validators.validateProduct


class Handler{

    fun handleProducts(rows: List<RawProductInfo>) : Results {
        val products = mutableListOf<Product>()
        val errors = mutableListOf<ErrorInfo>()

        for (row in rows) {
            try {
                val product = parseProduct(row)
                validateProduct(product)
                products.add(product)
            } catch (e: ProductParseException) {
                errors.add(ErrorInfo(
                    type = ErrorType.PARSING,
                    rowNumber = row.rowNum,
                    message = e.message ?: "Ошибка парсинга"
                ))
            } catch (e: ProductValidationException) {
                errors.add(ErrorInfo(
                    type = ErrorType.VALIDATION,
                    rowNumber = row.rowNum,
                    message = e.message ?: "Ошибка валидации"
                ))
            }

        }
        return Results(products, errors)
    }
}