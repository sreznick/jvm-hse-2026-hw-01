package exporters

import structures.Results
import java.io.File


class TextExporter : Exporter {
    override fun export(results: Results, path: String) {
        File(path).writeText(buildReport(results))
    }
}


fun buildReport(results: Results): String {
    return buildString {
        appendLine("=== СТАТИСТИКА ===")
        appendLine("Обработано строк: ${results.products.size + results.errors.size}")
        appendLine("Успешно загружено товаров: ${results.products.size}")
        appendLine("Ошибок парсинга: ${results.parsingErrorsCount}")
        appendLine("Ошибок валидации: ${results.validationErrorsCount}")

        appendLine("=== ОТЧЁТ ОБ ОШИБКАХ ===")
        for (error in results.errors) {
            appendLine("[ОШИБКА] Строка ${error.rowNumber}: ${error.message}")
        }
    }
}