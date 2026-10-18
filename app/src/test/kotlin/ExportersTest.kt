import kotlin.test.Test
import kotlin.test.assertContains
import exporters.JsonExporter
import exporters.XmlExporter
import exporters.TextExporter
import structures.Accessory
import structures.ErrorInfo
import structures.ErrorType
import structures.Results
import structures.Vehicle
import java.io.File
import kotlin.io.path.createTempFile
import kotlin.io.path.deleteIfExists


class ExportersTest {

    private fun createResults(): Results {
        val product = Accessory(
            id = 1,
            name = "Тестовый товар",
            priceRub = 100.0,
            weightKg = 2.0,
            vehicle = Vehicle.PASSENGER,
            material = "резина",
            color = null
        )

        val error = ErrorInfo(
            type = ErrorType.PARSING,
            rowNumber = 2,
            message = "Не удалось распарсить цену"
        )

        return Results(
            products = listOf(product),
            errors = listOf(error)
        )
    }

    @Test
    fun generatesJsonFile() {
        val path = createTempFile(suffix = ".json")

        try {
            JsonExporter().export(createResults(), path.toString())
            val content = File(path.toString()).readText()

            assertContains(content, "\"products\"")
            assertContains(content, "\"Тестовый товар\"")
            assertContains(content, "\"errors\"")
            assertContains(content, "Не удалось распарсить цену")
        } finally {
            path.deleteIfExists()
        }
    }

    @Test
    fun generatesXmlFile() {
        val path = createTempFile(suffix = ".xml")

        try {
            XmlExporter().export(createResults(), path.toString())
            val content = File(path.toString()).readText()

            assertContains(content, "<results>")
            assertContains(content, "<products>")
            assertContains(content, "Тестовый товар")
            assertContains(content, "<errors>")
            assertContains(content, "Не удалось распарсить цену")
        } finally {
            path.deleteIfExists()
        }
    }

    @Test
    fun generatesTextReport() {
        val path = createTempFile(suffix = ".txt")

        try {
            TextExporter().export(createResults(), path.toString())
            val content = File(path.toString()).readText()

            assertContains(content, "Обработано строк: 2")
            assertContains(content, "Успешно загружено товаров: 1")
            assertContains(content, "Ошибок парсинга: 1")
            assertContains(content, "Ошибок валидации: 0")
            assertContains(content, "Не удалось распарсить цену")
        } finally {
            path.deleteIfExists()
        }
    }
}