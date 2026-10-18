package org.processor

import org.errors.ParsingException

class CsvLineParser {

    fun parseLine(line: String): List<String> {
        val result = mutableListOf<String>()
        val currentField = StringBuilder()
        var inQuotes = false
        var i = 0

        while (i < line.length) {
            val char = line[i]

            when {
                char == '"' -> {
                    if (inQuotes && i + 1 < line.length && line[i + 1] == '"') {
                        currentField.append('"')
                        i++
                    } else {
                        inQuotes = !inQuotes
                    }
                }
                char == ',' && !inQuotes -> {
                    result.add(currentField.toString())
                    currentField.clear()
                }
                else -> {
                    currentField.append(char)
                }
            }
            i++
        }
        if (inQuotes) {
            throw ParsingException("Незакрытая кавычка в строке CSV")
        }
        result.add(currentField.toString())

        return result
    }
}