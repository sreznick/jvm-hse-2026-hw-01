package org.export

import org.processor.ProcessingResult

interface ResultExporter {
    val supportedFormat: String
    fun export(result: ProcessingResult): String
}