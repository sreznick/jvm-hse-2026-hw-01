package org.example.parser

import org.example.model.AccessoryProduct
import org.example.model.Color
import org.example.model.ErrorType
import org.example.model.OilProduct
import org.example.model.Season
import org.example.model.TireProduct
import org.example.model.VehicleType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RowParserTest {

    private fun parse(rawLine: String, lineNumber: Int = 1): RowResult {
        val fields = CsvParser.parseLine(rawLine)
        return RowParser.parse(fields, lineNumber)
    }

    // ---------- Корректные строки ----------

    @Test
    fun `parses valid oil row`() {
        val result = parse("1,\"Mobil 1, Synthetic\",oil,\$12.5,10lb,PASSENGER,volume=4")

        assertTrue(result is RowResult.Success)
        val product = result.product

        assertTrue(product is OilProduct)
        assertEquals(1, product.id)
        assertEquals("Mobil 1, Synthetic", product.name)
        assertEquals(1125.0, product.priceRub, 0.001)
        assertEquals(4.54, product.weightKg, 0.001)
        assertEquals(VehicleType.PASSENGER, product.vehicle)
        assertEquals(4.0, product.volume, 0.001)
    }

    @Test
    fun `parses valid oil row with rubles`() {
        val result = parse("2,Castrol Magnatec,oil,300.5руб,1.2kg,PASSENGER,viscosity=10W-40;volume=1")

        assertTrue(result is RowResult.Success)
        val product = result.product

        assertTrue(product is OilProduct)
        assertEquals(300.5, product.priceRub, 0.001)
        assertEquals(1.2, product.weightKg, 0.001)
        assertEquals(1.0, product.volume, 0.001)
    }

    @Test
    fun `parses valid tire row`() {
        val result = parse("3,\"Michelin Primacy\",tire,15\$,0.3kg,PASSENGER,diameter=16;season=SUMMER")

        assertTrue(result is RowResult.Success)
        val product = result.product

        assertTrue(product is TireProduct)
        assertEquals(3, product.id)
        assertEquals("Michelin Primacy", product.name)
        assertEquals(1350.0, product.priceRub, 0.001)
        assertEquals(0.3, product.weightKg, 0.001)
        assertEquals(16.0, product.diameter, 0.001)
        assertEquals(Season.SUMMER, product.season)
    }

    @Test
    fun `parses valid tire row with pounds and truck`() {
        val result = parse("4,Nokian Hakkapeliitta,tire,25руб,2lb,TRUCK,diameter=18;season=WINTER")

        assertTrue(result is RowResult.Success)
        val product = result.product

        assertTrue(product is TireProduct)
        assertEquals(25.0, product.priceRub, 0.001)
        assertEquals(0.908, product.weightKg, 0.001)
        assertEquals(VehicleType.TRUCK, product.vehicle)
        assertEquals(18.0, product.diameter, 0.001)
        assertEquals(Season.WINTER, product.season)
    }

    @Test
    fun `parses valid accessory row`() {
        val result = parse("5,Щетки стеклоочистителя,accessory,100руб,0.1kg,PASSENGER,material=резина;color=черный")

        assertTrue(result is RowResult.Success)
        val product = result.product

        assertTrue(product is AccessoryProduct)
        assertEquals(5, product.id)
        assertEquals(100.0, product.priceRub, 0.001)
        assertEquals("резина", product.material)
        assertEquals(Color.BLACK, product.color)
    }

    @Test
    fun `parses valid accessory row with quoted name`() {
        val result = parse("6,\"Bosch, Air Filter\",accessory,\$7.5,0.05kg,MOTORCYCLE,material=бумага;color=белый")

        assertTrue(result is RowResult.Success)
        val product = result.product

        assertTrue(product is AccessoryProduct)
        assertEquals("Bosch, Air Filter", product.name)
        assertEquals(675.0, product.priceRub, 0.001)
        assertEquals(VehicleType.MOTORCYCLE, product.vehicle)
        assertEquals("бумага", product.material)
        assertEquals(Color.WHITE, product.color)
    }

    // ---------- Некорректные строки ----------

    @Test
    fun `row 7 - bad price is parsing error`() {
        val result = parse("7,Масло,oil,abc,0.5kg,PASSENGER,viscosity=5W-30", lineNumber = 7)

        assertTrue(result is RowResult.Failure)
        assertEquals(7, result.error.lineNumber)
        assertEquals(ErrorType.PARSING, result.error.type)
        assertTrue(result.error.message.contains("цену"))
        assertTrue(result.error.message.contains("abc"))
    }

    @Test
    fun `row 8 - empty name is validation error`() {
        val result = parse("8,,tire,10\$,0.2kg,PASSENGER,diameter=16", lineNumber = 8)

        assertTrue(result is RowResult.Failure)
        assertEquals(8, result.error.lineNumber)
        assertEquals(ErrorType.VALIDATION, result.error.type)
        assertEquals("Пустое название товара", result.error.message)
    }

    @Test
    fun `row 9 - bad color is validation error`() {
        val result = parse("9,Присадка,accessory,50руб,0.1kg,PASSENGER,material=пластик;color=red", lineNumber = 9)

        assertTrue(result is RowResult.Failure)
        assertEquals(9, result.error.lineNumber)
        assertEquals(ErrorType.VALIDATION, result.error.type)
        assertEquals("Недопустимое значение 'color=red'", result.error.message)
    }

    @Test
    fun `row 10 - negative price is validation error`() {
        val result = parse("10,Шина,tire,-5\$,0.3kg,PASSENGER,diameter=17", lineNumber = 10)

        assertTrue(result is RowResult.Failure)
        assertEquals(10, result.error.lineNumber)
        assertEquals(ErrorType.VALIDATION, result.error.type)
        assertEquals("Цена не может быть отрицательной", result.error.message)
    }

    @Test
    fun `row 11 - bad volume is parsing error`() {
        val result = parse("11,Масло,oil,100руб,0.5kg,PASSENGER,viscosity=5W-30;volume=abc", lineNumber = 11)

        assertTrue(result is RowResult.Failure)
        assertEquals(11, result.error.lineNumber)
        assertEquals(ErrorType.PARSING, result.error.type)
        assertTrue(result.error.message.contains("Объем"))
        assertTrue(result.error.message.contains("abc"))
    }

    @Test
    fun `row 12 - unknown type is parsing error`() {
        val result = parse("12,Аккумулятор,battery,200руб,0.4kg,PASSENGER,material=свинец", lineNumber = 12)

        assertTrue(result is RowResult.Failure)
        assertEquals(12, result.error.lineNumber)
        assertEquals(ErrorType.PARSING, result.error.type)
        assertEquals("Неизвестный тип товара 'battery'", result.error.message)
    }

    // ---------- Дополнительные краевые случаи ----------

    @Test
    fun `missing volume for oil is parsing error`() {
        val result = parse("1,Масло,oil,100руб,1kg,PASSENGER", lineNumber = 1)

        assertTrue(result is RowResult.Failure)
        assertEquals(ErrorType.PARSING, result.error.type)
        assertEquals("Отсутствует обязательный параметр 'volume'", result.error.message)
    }

    @Test
    fun `missing diameter for tire is parsing error`() {
        val result = parse("1,Шина,tire,100руб,1kg,PASSENGER", lineNumber = 1)

        assertTrue(result is RowResult.Failure)
        assertEquals(ErrorType.PARSING, result.error.type)
        assertEquals("Отсутствует обязательный параметр 'diameter'", result.error.message)
    }

    @Test
    fun `unknown season for tire is validation error`() {
        val result = parse("1,Шина,tire,100руб,1kg,PASSENGER,diameter=16;season=AUTUMN", lineNumber = 1)

        assertTrue(result is RowResult.Failure)
        assertEquals(ErrorType.VALIDATION, result.error.type)
        assertEquals("Недопустимое значение 'season=AUTUMN'", result.error.message)
    }

    @Test
    fun `accessory without extras is valid`() {
        val result = parse("5,Щетки,accessory,100руб,0.1kg,PASSENGER", lineNumber = 5)

        assertTrue(result is RowResult.Success)
        val product = result.product
        assertTrue(product is AccessoryProduct)
        assertEquals(null, product.material)
        assertEquals(null, product.color)
    }

    @Test
    fun `unknown extra key is ignored`() {
        val result = parse("1,Масло,oil,100руб,1kg,PASSENGER,volume=4;width=10;height=20", lineNumber = 1)

        assertTrue(result is RowResult.Success)
        val product = result.product
        assertTrue(product is OilProduct)
        assertEquals(4.0, product.volume, 0.001)
    }

    @Test
    fun `too few fields is parsing error`() {
        val result = parse("1,Масло,oil", lineNumber = 1)

        assertTrue(result is RowResult.Failure)
        assertEquals(ErrorType.PARSING, result.error.type)
        assertTrue(result.error.message.contains("полей"))
    }
}