package org.autoshop.parser

import java.io.File

class CsvReader {

    fun read(filePath: String): List<RawRecord> {
        val file = File(filePath)
        if (!file.exists()) return emptyList()

        val lines = file.readLines()
        if (lines.isEmpty()) return emptyList()

        val records = mutableListOf<RawRecord>()

        // Начинаем с индекса 1, чтобы пропустить строку заголовка
        for (i in 1 until lines.size) {
            val line = lines[i]
            if (line.isBlank()) continue

            val columns = parseLine(line)

            // Если колонок меньше 7, строка некорректна структурно,
            // в реальном проекте здесь можно кидать ошибку парсинга
            val extraParams = if (columns.size >= 7) columns[6] else ""

            if (columns.size >= 6) {
                records.add(
                    RawRecord(
                        lineNumber = i, // +1 для удобства чтения пользователем (счет с 1)
                        id = columns[0],
                        // Убираем внешние кавычки и заменяем экранированные двойные на одинарные
                        name = columns[1].removeSurrounding("\"").replace("\"\"", "\""),
                        type = columns[2],
                        price = columns[3],
                        weight = columns[4],
                        vehicle = columns[5],
                        extra = extraParams
                    )
                )
            }
        }
        return records
    }

    private fun parseLine(line: String): List<String> {
        val result = mutableListOf<String>()
        var currentToken = StringBuilder()
        var inQuotes = false

        for (char in line) {
            if (char == '"') {
                inQuotes = !inQuotes
                currentToken.append(char) // Сохраняем кавычки, очистим их при создании объекта
            } else if (char == ',' && !inQuotes) {
                // Разделитель найден вне кавычек
                result.add(currentToken.toString())
                currentToken.clear()
            } else {
                currentToken.append(char)
            }
        }
        result.add(currentToken.toString()) // Добавляем последний элемент

        return result
    }
}