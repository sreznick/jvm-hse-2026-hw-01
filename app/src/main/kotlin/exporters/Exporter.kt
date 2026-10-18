package exporters

import structures.Results


interface Exporter {
    fun export(results: Results, path: String)
}