package org.autoshop.parser

data class RawRecord(
    val lineNumber: Int,
    val id: String,
    val name: String,
    val type: String,
    val price: String,
    val weight: String,
    val vehicle: String,
    val extra: String
)