package validators

import structures.Oil
import structures.Product


fun validateProduct(product: Product) {
    if (product.name.isBlank()) {
        throw ProductValidationException("Пустое название товара")
    }

    if (product.priceRub <= 0) {
        throw ProductValidationException("Цена должна быть положительной")
    }

    if (product.weightKg <= 0) {
        throw ProductValidationException("Вес должен быть положительным")
    }

    if (product is Oil && product.volume != null && product.volume <= 0) {
        throw ProductValidationException("Объём должен быть положительным")
    }
}