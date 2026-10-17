package org.example

import org.example.exporter.ExporterRegistry
import org.example.exporter.JsonExporter
import org.example.exporter.TxtExporter
import org.example.exporter.XmlExporter
import org.example.service.ProcessingService
import java.io.File

fun main(args: Array<String>) {
    val inputPath = args.getOrNull(0) ?: "input.csv"
    val format = args.getOrNull(1) ?: promptFormat()

    val registry = ExporterRegistry().apply {
        register(JsonExporter())
        register(XmlExporter())
        register(TxtExporter())
    }

    val exporter = registry.get(format)
    if (exporter == null) {
        println("Формат '$format' не поддерживается. Доступно: ${registry.availableFormats()}")
        return
    }

    val file = File(inputPath)
    if (!file.exists()) {
        println("Файл '$inputPath' не найден")
        return
    }

    val lines = file.readLines()
        .filter { it.isNotBlank() }
        .drop(1)

    val service = ProcessingService()
    val result = service.process(lines)

    printStatistics(result)
    printErrors(result)

    val output = service.export(result, exporter)
    val outputFile = File("output.${exporter.format}")
    outputFile.writeText(output)
    println()
    println("Результат сохранён в ${outputFile.absolutePath}")
}

private fun promptFormat(): String {
    println("Выберите формат сохранения (json / xml / txt):")
    return readlnOrNull()?.trim()?.lowercase() ?: "txt"
}

private fun printStatistics(result: org.example.service.ProcessingResult) {
    println("Обработано строк: ${result.totalLines}")
    println("Успешно загружено товаров: ${result.products.size}")
    println("Ошибок парсинга: ${result.parsingErrorsCount}")
    println("Ошибок валидации: ${result.validationErrorsCount}")
    println()
    println("Отчет об ошибках:")
    println()
}

private fun printErrors(result: org.example.service.ProcessingResult) {
    if (result.errors.isEmpty()) {
        println("Ошибок нет.")
        return
    }
    result.errors.forEach {
        println("[ОШИБКА] Строка ${it.lineNumber}: ${it.message}")
        println()
    }
}