import kotlin.test.Test
import kotlin.test.assertEquals
import structures.ErrorType
import structures.RawProductInfo

class HandlerTest {

    @Test
    fun handlesValidProduct() {
        val rows = listOf(
            createRow(rowNum = 1, type = "oil")
        )

        val results = Handler().handleProducts(rows)

        assertEquals(1, results.products.size)
        assertEquals(0, results.errors.size)
    }

    @Test
    fun recordsParsingError() {
        val rows = listOf(
            createRow(rowNum = 2, type = "battery")
        )

        val results = Handler().handleProducts(rows)

        assertEquals(0, results.products.size)
        assertEquals(1, results.errors.size)
        assertEquals(ErrorType.PARSING, results.errors[0].type)
        assertEquals(2, results.errors[0].rowNumber)
    }

    @Test
    fun recordsValidationError() {
        val rows = listOf(
            createRow(rowNum = 3, type = "oil", name = "")
        )

        val results = Handler().handleProducts(rows)

        assertEquals(0, results.products.size)
        assertEquals(1, results.errors.size)
        assertEquals(ErrorType.VALIDATION, results.errors[0].type)
        assertEquals(3, results.errors[0].rowNumber)
    }

    @Test
    fun continuesProcessingAfterErrors() {
        val rows = listOf(
            createRow(rowNum = 1, type = "oil"),
            createRow(rowNum = 2, type = "battery"),
            createRow(rowNum = 3, type = "accessory", name = "")
        )

        val results = Handler().handleProducts(rows)

        assertEquals(1, results.products.size)
        assertEquals(2, results.errors.size)
        assertEquals(1, results.parsingErrorsCount)
        assertEquals(1, results.validationErrorsCount)
    }

    @Test
    fun countsErrorsCorrectly() {
        val rows = listOf(
            createRow(rowNum = 1, type = "battery"),
            createRow(rowNum = 2, type = "oil", name = ""),
            createRow(rowNum = 3, type = "tire"),
            createRow(rowNum = 4, type = "battery")
        )

        val results = Handler().handleProducts(rows)

        assertEquals(1, results.products.size)
        assertEquals(3, results.errors.size)
        assertEquals(2, results.parsingErrorsCount)
        assertEquals(1, results.validationErrorsCount)
    }

    private fun createRow(
        rowNum: Int,
        type: String,
        name: String = "Тестовый товар"
    ): RawProductInfo {
        return RawProductInfo(
            rowNum = rowNum,
            id = rowNum.toString(),
            name = name,
            type = type,
            price = "100руб",
            weight = "2kg",
            vehicle = "PASSENGER",
            extra = ""
        )
    }
}
