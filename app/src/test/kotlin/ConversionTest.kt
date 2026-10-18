    import kotlin.test.Test
    import kotlin.test.assertEquals
    import kotlin.test.assertFailsWith
    import parsers.parsePrice
    import parsers.parseWeight
    import validators.ProductParseException


    class ConversionTest {

        @Test
        fun convertsDollarsToRubles() {
            assertEquals(900.0, parsePrice("$10"))
        }

        @Test
        fun keepsRublesUnchanged() {
            assertEquals(100.0, parsePrice("100руб"))
        }

        @Test
        fun convertsPoundsToKilograms() {
            assertEquals(0.454, parseWeight("1lb"), 0.000001)
        }

        @Test
        fun keepsKilogramsUnchanged() {
            assertEquals(2.0, parseWeight("2kg"))
        }

        @Test
        fun rejectsInvalidPrice() {
            assertFailsWith<ProductParseException> {
                parsePrice("abc")
            }
        }

        @Test
        fun rejectsInvalidWeight() {
            assertFailsWith<ProductParseException> {
                parseWeight("abc")
            }
        }
    }