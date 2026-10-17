package org.example.parser

import kotlin.test.Test
import kotlin.test.assertEquals

class CsvParserTest {

    @Test
    fun `parses simple line without quotes`() {
        val result = CsvParser.parseLine("1,Castrol Magnatec,oil,300.5руб,1.2kg,PASSENGER,volume=1")
        assertEquals(
            listOf("1", "Castrol Magnatec", "oil", "300.5руб", "1.2kg", "PASSENGER", "volume=1"),
            result,
        )
    }

    @Test
    fun `keeps comma inside quoted field`() {
        val result = CsvParser.parseLine("1,\"Mobil 1, Synthetic\",oil,\$12.5,10lb,PASSENGER,volume=4")
        assertEquals(
            listOf("1", "Mobil 1, Synthetic", "oil", "\$12.5", "10lb", "PASSENGER", "volume=4"),
            result,
        )
    }

    @Test
    fun `unescapes doubled quotes`() {
        val result = CsvParser.parseLine("1,\"Он сказал \"\"привет\"\"\",oil,10руб,1kg,PASSENGER,")
        assertEquals(
            listOf("1", "Он сказал \"привет\"", "oil", "10руб", "1kg", "PASSENGER", ""),
            result,
        )
    }

    @Test
    fun `keeps empty field in the middle`() {
        val result = CsvParser.parseLine("8,,tire,10\$,0.2kg,PASSENGER,diameter=16")
        assertEquals(
            listOf("8", "", "tire", "10\$", "0.2kg", "PASSENGER", "diameter=16"),
            result,
        )
    }

    @Test
    fun `keeps trailing empty field`() {
        val result = CsvParser.parseLine("1,name,oil,10руб,1kg,PASSENGER,")
        assertEquals(
            listOf("1", "name", "oil", "10руб", "1kg", "PASSENGER", ""),
            result,
        )
    }

    @Test
    fun `parses quoted field at the start`() {
        val result = CsvParser.parseLine("\"a,b\",c,d")
        assertEquals(listOf("a,b", "c", "d"), result)
    }

    @Test
    fun `parses quoted field at the end`() {
        val result = CsvParser.parseLine("a,b,\"c,d\"")
        assertEquals(listOf("a", "b", "c,d"), result)
    }

    @Test
    fun `handles line with only separators`() {
        val result = CsvParser.parseLine(",,,,,,")
        assertEquals(listOf("", "", "", "", "", "", ""), result)
    }

    @Test
    fun `parses multiple quoted fields with commas`() {
        val result = CsvParser.parseLine("\"a,b\",\"c,d\",\"e,f\"")
        assertEquals(listOf("a,b", "c,d", "e,f"), result)
    }
}