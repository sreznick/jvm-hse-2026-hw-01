package org.export

import org.processor.ProcessingResult
import java.util.Locale

class TextReportExporter : ResultExporter {
    override val supportedFormat: String = "txt"

    override fun export(result: ProcessingResult): String = buildString {
        appendLine("=== ОТЧЕТ ОБ ОБРАБОТКЕ ДАННЫХ ===")
        appendLine("Обработано строк: ${result.totalRowsProcessed}")
        appendLine("Успешно загружено товаров: ${result.loadedProductsCount}")
        appendLine("Ошибок парсинга: ${result.parsingErrorsCount}")
        appendLine("Ошибок валидации: ${result.validationErrorsCount}")

        if (result.errors.isNotEmpty()) {
            appendLine()
            appendLine("--- Список ошибок ---")
            for (error in result.errors) {
                appendLine(error.format())
            }
        }

        if (result.products.isNotEmpty()) {
            appendLine()
            appendLine("--- Загруженные товары ---")
            for (p in result.products) {
                val priceFormatted = String.format(Locale.US, "%.2f", p.priceRub)
                val weightFormatted = String.format(Locale.US, "%.3f", p.weightKg)
                val extraStr = p.extra.entries.joinToString(", ") { "${it.key}=${it.value}" }
                appendLine("[#${p.id}] ${p.name} | Тип: ${p.type.csvName} | Цена: $priceFormatted руб | Вес: $weightFormatted кг | Авто: ${p.vehicle} | Доп: {$extraStr}")
            }
        }
    }
}