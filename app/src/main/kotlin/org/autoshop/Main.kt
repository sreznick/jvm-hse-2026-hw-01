package org.autoshop

import org.autoshop.export.*
import org.autoshop.parser.CsvReader
import org.autoshop.processing.*
import org.autoshop.service.ImportService

fun main(args: Array<String>) {

    val format = args.firstOrNull()?.lowercase() ?: "json"
    val inputFile = "1.csv"
    val outputFile = "output.$format"

    val registry = ExporterRegistry().apply {
        register("json", JsonExporter())
        register("xml", XmlExporter())
        register("txt", TextExporter())
    }

    val normalizer = Normalizer()
    val validator = Validator()
    val productFactory = ProductFactory(normalizer, validator)
    val csvReader = CsvReader()

    val importService = ImportService(csvReader, productFactory, registry)

    val result = try {
        importService.process(inputFile, format, outputFile)
    } catch (e: Exception) {
        println("Критическая ошибка выполнения: ${e.message}")
        return
    }

    println("### Статистика")
    println("Обработано строк: ${result.stats.totalProcessed}")
    println("Успешно загружено товаров: ${result.stats.successfullyLoaded}")
    println("Ошибок парсинга: ${result.stats.parseErrors}")
    println("Ошибок валидации: ${result.stats.validationErrors}")
    println("\n### Отчет об ошибках")

    if (result.errors.isEmpty()) {
        println("Ошибок нет.")
    } else {
        result.errors.forEach { error ->
            println("[ОШИБКА] Строка ${error.lineNumber}: ${error.message}")
        }
    }
}