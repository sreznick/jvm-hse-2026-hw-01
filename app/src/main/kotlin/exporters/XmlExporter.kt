package exporters

import structures.Accessory
import structures.Oil
import structures.Product
import structures.Results
import structures.Tire
import java.io.FileOutputStream
import javax.xml.stream.XMLOutputFactory


class XmlExporter : Exporter {
    override fun export(results: Results, path: String) {
        val factory = XMLOutputFactory.newFactory()

        FileOutputStream(path).use { output ->
            val writer = factory.createXMLStreamWriter(output, "UTF-8")

            writer.writeStartElement("results")


            writer.writeStartElement("products")
            for (product in results.products) {
                writer.writeStartElement("product")

                writeElement(writer, "id", product.id.toString())
                writeElement(writer, "name", product.name)
                writeElement(writer, "type", productType(product))
                writeElement(writer, "priceRub", product.priceRub.toString())
                writeElement(writer, "weightKg", product.weightKg.toString())
                writeElement(writer, "vehicle", product.vehicle.name)

                when (product) {
                    is Oil -> {
                        product.volume?.let {
                            writeElement(writer, "volume", it.toString())
                        }
                    }

                    is Tire -> {
                        product.diameter?.let {
                            writeElement(writer, "diameter", it.toString())
                        }
                        product.season?.let {
                            writeElement(writer, "season", it.name)
                        }
                    }

                    is Accessory -> {
                        product.material?.let {
                            writeElement(writer, "material", it)
                        }
                        product.color?.let {
                            writeElement(writer, "color", it.name)
                        }
                    }
                }
                writer.writeEndElement()
            }
            writer.writeEndElement()


            writer.writeStartElement("errors")
            for (error in results.errors) {
                writer.writeStartElement("error")

                writeElement(writer, "type", error.type.name)
                writeElement(writer, "rowNumber", error.rowNumber.toString())
                writeElement(writer, "message", error.message)

                writer.writeEndElement()
            }

            writer.writeEndElement()
            writer.writeEndElement()

            writer.close()
        }
    }


    private fun productType(product: Product): String {
        return when (product) {
            is Oil -> "OIL"
            is Tire -> "TIRE"
            is Accessory -> "ACCESSORY"
        }
    }

    private fun writeElement(
        writer: javax.xml.stream.XMLStreamWriter,
        name: String,
        value: String
    ) {
        writer.writeStartElement(name)
        writer.writeCharacters(value)
        writer.writeEndElement()
    }
}