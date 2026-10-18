    package structures

    import kotlinx.serialization.Serializable


    @Serializable
    enum class Type {
        OIL,
        TIRE,
        ACCESSORY
    }

    @Serializable
    enum class Vehicle {
        PASSENGER,
        TRUCK,
        MOTORCYCLE
    }

    @Serializable
    enum class Season {
        SUMMER,
        WINTER,
        ALL_SEASON
    }

    @Serializable
    enum class Color {
        BLACK,
        WHITE,
        SILVER
    }

    @Serializable
    enum class ErrorType {
        PARSING,
        VALIDATION
    }