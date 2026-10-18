package org.tests

import org.errors.ParsingException
import org.errors.ValidationException
import org.processor.ProductValidator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ProductValidatorTest {

    private val validator = ProductValidator()

    @Test
    fun `should successfully validate and build product`() {
        val product = validator.validateAndBuildFields(
            rawId = "1",
            rawName = "Mobil 1",
            rawType = "oil",
            rawPrice = "$10",
            rawWeight = "5kg",
            rawVehicle = "PASSENGER",
            rawExtra = "volume=4"
        )

        assertEquals(1, product.id)
        assertEquals("Mobil 1", product.name)
        assertEquals(900.0, product.priceRub)
        assertEquals(5.0, product.weightKg)
    }

    @Test
    fun `should throw validation exception when values are negative`() {
        assertFailsWith<ValidationException> {
            validator.validateAndBuildFields(
                rawId = "1",
                rawName = "Mobil 1",
                rawType = "oil",
                rawPrice = "$-10",
                rawWeight = "5kg",
                rawVehicle = "PASSENGER",
                rawExtra = "volume=4"
            )
        }

        assertFailsWith<ValidationException> {
            validator.validateAndBuildFields(
                rawId = "1",
                rawName = "Mobil 1",
                rawType = "oil",
                rawPrice = "$10",
                rawWeight = "-5kg",
                rawVehicle = "PASSENGER",
                rawExtra = "volume=4"
            )
        }

        assertFailsWith<ValidationException> {
            validator.validateAndBuildFields(
                rawId = "1",
                rawName = "Mobil 1",
                rawType = "oil",
                rawPrice = "$10",
                rawWeight = "-5kg",
                rawVehicle = "PASSENGER",
                rawExtra = "volume=4"
            )
        }
    }

    @Test
    fun `should ignore unknown extra keys and successfully parse`() {
        val product = validator.validateAndBuildFields(
            rawId = "1",
            rawName = "Mobil 1",
            rawType = "oil",
            rawPrice = "$10",
            rawWeight = "5kg",
            rawVehicle = "PASSENGER",
            rawExtra = "unknown_key=some_value;volume=4;color=белый"
        )

        assertEquals(1, product.id)
        assertEquals(4.0, product.extra["volume"])
        assertEquals(null, product.extra["unknown_key"])
        assertEquals(null, product.extra["color"])
    }

    @Test
    fun `should throw parsing exception when extra number has invalid format`() {
        assertFailsWith<ParsingException> {
            validator.validateAndBuildFields(
                rawId = "1",
                rawName = "Mobil 1",
                rawType = "oil",
                rawPrice = "$10",
                rawWeight = "5kg",
                rawVehicle = "PASSENGER",
                rawExtra = "volume=abc"
            )
        }
    }

    @Test
    fun `should throw validation exception when allowed value is invalid`() {
        assertFailsWith<ValidationException> {
            validator.validateAndBuildFields(
                rawId = "4",
                rawName = "Ароматизатор",
                rawType = "accessory",
                rawPrice = "100руб",
                rawWeight = "0.05kg",
                rawVehicle = "PASSENGER",
                rawExtra = "color=red;material=пластик"
            )
        }
    }

    @Test
    fun `should throw parsing exception when id is invalid`() {
        assertFailsWith<ParsingException> {
            validator.validateAndBuildFields(
                rawId = "not_an_id",
                rawName = "Mobil 1",
                rawType = "oil",
                rawPrice = "$10",
                rawWeight = "5kg",
                rawVehicle = "PASSENGER",
                rawExtra = ""
            )
        }
    }
}