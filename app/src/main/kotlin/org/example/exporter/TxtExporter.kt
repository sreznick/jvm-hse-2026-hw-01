package org.example.exporter

import org.example.model.AccessoryProduct
import org.example.model.OilProduct
import org.example.model.Product
import org.example.model.TireProduct

class TxtExporter : Exporter {

    override val format: String = "txt"

    override fun export(products: List<Product>): String {
        val sb = StringBuilder()
        sb.appendLine("=== Отчёт о товарах автомагазина ===")
        sb.appendLine("Всего товаров: ${products.size}")
        sb.appendLine()
        products.forEach { sb.appendLine(formatProduct(it)) }
        return sb.toString()
    }

    private fun formatProduct(p: Product): String = when (p) {
        is OilProduct ->
            "Масло #${p.id}: ${p.name} | ${p.priceRub} руб | ${p.weightKg} кг | " +
                    "${p.vehicle} | объём ${p.volume} л"

        is TireProduct ->
            "Шина #${p.id}: ${p.name} | ${p.priceRub} руб | ${p.weightKg} кг | " +
                    "${p.vehicle} | диаметр ${p.diameter}\" | сезон ${p.season}"

        is AccessoryProduct ->
            "Аксессуар #${p.id}: ${p.name} | ${p.priceRub} руб | ${p.weightKg} кг | " +
                    "${p.vehicle} | материал ${p.material ?: "-"} | цвет ${p.color?.ru ?: "-"}"
    }
}