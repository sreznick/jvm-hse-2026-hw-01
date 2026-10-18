package exporters

import kotlinx.serialization.json.Json
import structures.Results
import java.io.File


class JsonExporter : Exporter {
    override fun export(results: Results, path: String) {
        File(path).writeText(Json.encodeToString(results))
    }
}