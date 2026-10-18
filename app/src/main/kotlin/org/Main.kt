package org

import org.export.ExportService
import org.export.JsonExporter
import org.export.TextReportExporter
import org.export.XmlExporter
import org.processor.CsvFileProcessor
import java.io.PrintStream

fun main(args: Array<String>) {
    System.setOut(PrintStream(System.out, true, Charsets.UTF_8))
    val filePath = args.firstOrNull() ?: "input.csv"

    val processor = CsvFileProcessor()

    val result = try {
        processor.processFile(filePath)
    } catch (e: IllegalArgumentException) {
        println("Ошибка запуска: ${e.message}")
        return
    }

    println("Обработано строк: ${result.totalRowsProcessed}")
    println("Успешно загружено товаров: ${result.loadedProductsCount}")
    println("Ошибок парсинга: ${result.parsingErrorsCount}")
    println("Ошибок валидации: ${result.validationErrorsCount}")

    if (result.errors.isNotEmpty()) {
        println()
        for (error in result.errors) {
            println(error.format())
        }
    }

    val exportService = ExportService(
        listOf(
            JsonExporter(),
            XmlExporter(),
            TextReportExporter()
        )
    )

    println("Доступные форматы для сохранения: json, xml, txt")
    print("Введите желаемый формат: ")
    val formatChoice = readln().trim().lowercase()
    try {
        val fileName = "output.$formatChoice"
        exportService.saveToFile(result, fileName)
        println("Файл успешно сохранен: $fileName\n")
    } catch (e: Exception) {
        println("Ошибка сохранения: ${e.message}\n")
    }
}