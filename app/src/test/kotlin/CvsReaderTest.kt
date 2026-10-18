import kotlin.test.assertEquals
import kotlin.test.Test
import java.io.File


class CsvReaderTest {

    @Test
    fun readsCorrectCsvFields() {
        val file = createTempCsv(
            """
            id,name,type,price,weight,vehicle,extra
            1,Масло,OIL,$10,2kg,PASSENGER,volume=4
            """.trimIndent()
        )

        try {
            val rows = CsvReader().read(file.absolutePath)

            assertEquals(1, rows.size)
            assertEquals("1", rows[0].id)
            assertEquals("Масло", rows[0].name)
            assertEquals("OIL", rows[0].type)
            assertEquals("$10", rows[0].price)
            assertEquals("2kg", rows[0].weight)
            assertEquals("PASSENGER", rows[0].vehicle)
            assertEquals("volume=4", rows[0].extra)
            assertEquals(1, rows[0].rowNum)
        } finally {
            file.delete()
        }
    }

    @Test
    fun readsQuotedFieldWithComma() {
        val file = createTempCsv(
            """
            id,name,type,price,weight,vehicle,extra
            1,"Масло, синтетическое",OIL,$10,2kg,PASSENGER,volume=4
            """.trimIndent()
        )

        try {
            val rows = CsvReader().read(file.absolutePath)

            assertEquals(1, rows.size)
            assertEquals("Масло, синтетическое", rows[0].name)
        } finally {
            file.delete()
        }
    }

    @Test
    fun readsQuotesInsideQuotedField() {
        val file = kotlin.io.path.createTempFile(suffix = ".csv").toFile()

        try {
            file.writeText(
            """
            id,name,type,price,weight,vehicle,extra
            1,"Масло ""Премиум"", 5 л",oil,100руб,2kg,PASSENGER,
            """.trimIndent()
            )

            val rows = CsvReader().read(file.absolutePath)

            assertEquals(1, rows.size)
            assertEquals("Масло \"Премиум\", 5 л", rows[0].name)
        } finally {
            file.delete()
        }
    }


    private fun createTempCsv(content: String): File {
        return File.createTempFile("products-", ".csv").apply {
            writeText(content)
        }
    }
}