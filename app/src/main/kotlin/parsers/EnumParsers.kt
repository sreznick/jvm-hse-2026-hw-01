package parsers

import structures.Type
import structures.Vehicle
import validators.ProductParseException


fun parseProductType(type: String) : Type {
    return when (type.uppercase()) {
        "OIL" -> Type.OIL
        "TIRE" -> Type.TIRE
        "ACCESSORY" -> Type.ACCESSORY
        else -> {
            throw ProductParseException("Неизвестный тип товара '$type'")
        }
    }
}


fun parseVehicle(veh : String) : Vehicle {
    return when (veh.uppercase()) {
        "PASSENGER" -> Vehicle.PASSENGER
        "MOTORCYCLE" -> Vehicle.MOTORCYCLE
        "TRUCK" -> Vehicle.TRUCK
        else -> {
            throw ProductParseException("Неизвестный тип транспорта '$veh'")
        }
    }
}

