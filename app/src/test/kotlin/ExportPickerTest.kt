import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertNull
import exporters.ExporterPicker
import exporters.JsonExporter
import exporters.XmlExporter
import exporters.TextExporter


class ExporterPickerTest {

    private val picker = ExporterPicker()

    @Test
    fun returnsJsonExporter() {
        assertIs<JsonExporter>(picker.getExporter("json"))
    }

    @Test
    fun returnsXmlExporter() {
        assertIs<XmlExporter>(picker.getExporter("xml"))
    }

    @Test
    fun returnsTextExporter() {
        assertIs<TextExporter>(picker.getExporter("txt"))
    }

    @Test
    fun returnsNullForUnknownFormat() {
        assertNull(picker.getExporter("pdf"))
    }

    @Test
    fun acceptsUppercaseFormat() {
        assertIs<JsonExporter>(picker.getExporter("JSON"))
    }
}
