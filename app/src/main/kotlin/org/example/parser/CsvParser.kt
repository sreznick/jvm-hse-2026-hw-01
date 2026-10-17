package org.example.parser

object CsvParser {

    fun parseLine(line: String): List<String> {
        val fields = mutableListOf<String>()
        val current = StringBuilder()
        var inQuotes = false
        var i = 0

        while (i < line.length) {
            val c = line[i]

            if (inQuotes) {
                when {
                    c == '"' && i + 1 < line.length && line[i + 1] == '"' -> {
                        current.append('"')
                        i += 2
                        continue
                    }
                    c == '"' -> {
                        inQuotes = false
                    }
                    else -> current.append(c)
                }
            } else {
                when (c) {
                    '"' -> inQuotes = true
                    ',' -> {
                        fields.add(current.toString())
                        current.clear()
                    }
                    else -> current.append(c)
                }
            }
            i++
        }

        fields.add(current.toString())
        return fields
    }
}