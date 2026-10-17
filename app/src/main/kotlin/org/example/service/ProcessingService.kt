package org.example.service

import org.example.exporter.Exporter
import org.example.model.ParseError
import org.example.model.Product
import org.example.parser.CsvParser
import org.example.parser.RowParser
import org.example.parser.RowResult

class ProcessingService {

    fun process(lines: List<String>): ProcessingResult {
        val products = mutableListOf<Product>()
        val errors = mutableListOf<ParseError>()

        lines.forEachIndexed { index, line ->
            val lineNumber = index + 1
            val fields = CsvParser.parseLine(line)
            when (val result = RowParser.parse(fields, lineNumber)) {
                is RowResult.Success -> products += result.product
                is RowResult.Failure -> errors += result.error
            }
        }

        return ProcessingResult(
            totalLines = lines.size,
            products = products,
            errors = errors,
        )
    }

    fun export(result: ProcessingResult, exporter: Exporter): String =
        exporter.export(result.products)
}