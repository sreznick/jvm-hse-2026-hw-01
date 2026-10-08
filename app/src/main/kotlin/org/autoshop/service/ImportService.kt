package org.autoshop.service

import org.autoshop.domain.*
import org.autoshop.export.ExporterRegistry
import org.autoshop.parser.CsvReader
import org.autoshop.processing.ProductFactory
import org.autoshop.processing.ValidationException

class ImportService(
    private val csvReader: CsvReader,
    private val productFactory: ProductFactory,
    private val exporterRegistry: ExporterRegistry
) {
    fun process(inputFilePath: String, format: String, outputFilePath: String): ImportResult {
        val stats = ImportStatistics()
        val products = mutableListOf<Product>()
        val errors = mutableListOf<ErrorRecord>()

        val rawRecords = csvReader.read(inputFilePath)

        for (record in rawRecords) {
            stats.totalProcessed++

            productFactory.createProduct(record)
                .onSuccess { product ->
                    products.add(product)
                    stats.successfullyLoaded++
                }
                .onFailure { exception ->
                    val errorType = if (exception is ValidationException) {
                        stats.validationErrors++
                        ErrorType.VALIDATION_ERROR
                    } else {
                        stats.parseErrors++
                        ErrorType.PARSE_ERROR
                    }

                    errors.add(
                        ErrorRecord(
                            lineNumber = record.lineNumber,
                            type = errorType,
                            message = exception.message ?: "Неизвестная ошибка"
                        )
                    )
                }
        }

        if (products.isNotEmpty()) {
            val exporter = exporterRegistry.getExporter(format)
            exporter.export(products, outputFilePath)
        }

        return ImportResult(products, errors, stats)
    }
}