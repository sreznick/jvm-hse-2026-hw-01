import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import parsers.parseProduct
import parsers.parseProductType
import parsers.parseVehicle
import structures.RawProductInfo
import structures.Type
import structures.Vehicle
import validators.ProductParseException
import validators.ProductValidationException


class ProductParserTest {

    @Test
    fun parsesOilType() {
        assertEquals(Type.OIL, parseProductType("oil"))
    }

    @Test
    fun parsesTireType() {
        assertEquals(Type.TIRE, parseProductType("tire"))
    }

    @Test
    fun parsesAccessoryType() {
        assertEquals(Type.ACCESSORY, parseProductType("accessory"))
    }

    @Test
    fun rejectsUnknownProductType() {
        assertFailsWith<ProductParseException> {
            parseProductType("battery")
        }
    }

    @Test
    fun parsesPassengerVehicle() {
        assertEquals(Vehicle.PASSENGER, parseVehicle("PASSENGER"))
    }

    @Test
    fun parsesTruckVehicle() {
        assertEquals(Vehicle.TRUCK, parseVehicle("TRUCK"))
    }

    @Test
    fun parsesMotorcycleVehicle() {
        assertEquals(Vehicle.MOTORCYCLE, parseVehicle("MOTORCYCLE"))
    }

    @Test
    fun rejectsUnknownVehicle() {
        assertFailsWith<ProductParseException> {
            parseVehicle("BICYCLE")
        }
    }

    @Test
    fun rejectsNonNumericOilVolume() {
        val row = createRow(
            type = "oil",
            extra = "volume=abc"
        )

        assertFailsWith<ProductParseException> {
            parseProduct(row)
        }
    }

    @Test
    fun rejectsInvalidAccessoryColor() {
        val row = createRow(
            type = "accessory",
            extra = "color=red"
        )

        assertFailsWith<ProductValidationException> {
            parseProduct(row)
        }
    }

    @Test
    fun rejectsInvalidTireSeason() {
        val row = createRow(
            type = "tire",
            extra = "season=SPRING"
        )

        assertFailsWith<ProductValidationException> {
            parseProduct(row)
        }
    }

    private fun createRow(
        type: String,
        extra: String = "",
        vehicle: String = "PASSENGER"
    ): RawProductInfo {
        return RawProductInfo(
            rowNum = 1,
            id = "1",
            name = "Тестовый товар",
            type = type,
            price = "100руб",
            weight = "2kg",
            vehicle = vehicle,
            extra = extra
        )
    }
}
