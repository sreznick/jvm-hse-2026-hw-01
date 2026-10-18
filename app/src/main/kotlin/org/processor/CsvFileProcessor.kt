package org.processor

import org.errors.ErrorType
import org.errors.ParsingException
import org.errors.ErrorRow
import org.errors.ValidationException
import org.products.Product
import java.io.File

class CsvFileProcessor(
    private val lineParser: CsvLineParser = CsvLineParser(),
    private val validator: ProductValidator = ProductValidator()
) {

    fun processFile(filePath: String): ProcessingResult {
        val file = File(filePath)
        if (!file.exists()) {
            throw IllegalArgumentException("Файл не найден: $filePath")
        }

        val lines = file.readLines(Charsets.UTF_8)
        return processLines(lines)
    }

    fun processLines(lines: List<String>): ProcessingResult {
        if (lines.isEmpty()) {
            return ProcessingResult(0, emptyList(), emptyList())
        }

        val validProducts = mutableListOf<Product>()
        val errors = mutableListOf<ErrorRow>()
        var processedCount = 0

        val dataLines = lines.drop(1)

        for ((index, line) in dataLines.withIndex()) {

            if (line.isBlank()) continue

            val lineNumber = index + 1
            processedCount++

            try {
                val columns = lineParser.parseLine(line)
                if (columns.size != 7) {
                    throw ParsingException("Неверное количество колонок: ожидалось 7, получено ${columns.size}")
                }

                val product = validator.validateAndBuildFields(
                    rawId = columns[0],
                    rawName = columns[1],
                    rawType = columns[2],
                    rawPrice = columns[3],
                    rawWeight = columns[4],
                    rawVehicle = columns[5],
                    rawExtra = columns[6]
                )
                validProducts.add(product)

            } catch (e: ParsingException) {
                errors.add(ErrorRow(lineNumber = lineNumber, type = ErrorType.PARSING, message = e.message))
            } catch (e: ValidationException) {
                errors.add(ErrorRow(lineNumber = lineNumber, type = ErrorType.VALIDATION, message = e.message))
            }
        }

        return ProcessingResult(
            totalRowsProcessed = processedCount,
            products = validProducts,
            errors = errors
        )
    }
}