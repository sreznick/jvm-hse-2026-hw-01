import kotlin.test.Test
import kotlin.test.assertFailsWith
import structures.Accessory
import structures.Oil
import structures.Vehicle
import validators.ProductValidationException
import validators.validateProduct


class ProductValidationTest {

    @Test
    fun acceptsValidProduct() {
        val product = Accessory(
            id = 1,
            name = "Коврик",
            priceRub = 1000.0,
            weightKg = 2.0,
            vehicle = Vehicle.PASSENGER,
            material = "Резина",
            color = null
        )

        validateProduct(product)
    }

    @Test
    fun rejectsEmptyName() {
        val product = Accessory(
            id = 1,
            name = "",
            priceRub = 1000.0,
            weightKg = 2.0,
            vehicle = Vehicle.PASSENGER,
            material = null,
            color = null
        )

        assertFailsWith<ProductValidationException> {
            validateProduct(product)
        }
    }

    @Test
    fun rejectsNonPositivePrice() {
        val product = Accessory(
            id = 1,
            name = "Коврик",
            priceRub = 0.0,
            weightKg = 2.0,
            vehicle = Vehicle.PASSENGER,
            material = null,
            color = null
        )

        assertFailsWith<ProductValidationException> {
            validateProduct(product)
        }
    }

    @Test
    fun rejectsNonPositiveWeight() {
        val product = Accessory(
            id = 1,
            name = "Коврик",
            priceRub = 1000.0,
            weightKg = -1.0,
            vehicle = Vehicle.PASSENGER,
            material = null,
            color = null
        )

        assertFailsWith<ProductValidationException> {
            validateProduct(product)
        }
    }

    @Test
    fun rejectsNonPositiveOilVolume() {
        val product = Oil(
            id = 2,
            name = "Масло",
            priceRub = 500.0,
            weightKg = 1.0,
            vehicle = Vehicle.PASSENGER,
            volume = 0.0
        )

        assertFailsWith<ProductValidationException> {
            validateProduct(product)
        }
    }
}