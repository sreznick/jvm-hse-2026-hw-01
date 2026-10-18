package parsers


import structures.Accessory
import structures.Oil
import structures.Product
import structures.RawProductInfo
import structures.Tire
import structures.Type
import validators.ProductParseException


fun parseProduct(raw : RawProductInfo) : Product {
    val extra = parseExtra(raw.extra)

    val id: Int
    try {
        id = raw.id.toInt()
    } catch (e: NumberFormatException) {
        throw ProductParseException("Не удалось распарсить id '${raw.id}'")
    }

    val type = parseProductType(raw.type)
    return when (type) {
        Type.OIL -> Oil(
            id = id,
            name = raw.name,
            priceRub = parsePrice(raw.price),
            weightKg = parseWeight(raw.weight),
            vehicle = parseVehicle(raw.vehicle),
            volume = parseExtraOilVolume(extra)
        )

        Type.TIRE -> Tire(
            id = id,
            name = raw.name,
            priceRub = parsePrice(raw.price),
            weightKg = parseWeight(raw.weight),
            vehicle = parseVehicle(raw.vehicle),
            diameter = parseExtraTireDiameter(extra),
            season = parseExtraTireSeason(extra)
        )

        Type.ACCESSORY -> Accessory(
            id = id,
            name = raw.name,
            priceRub = parsePrice(raw.price),
            weightKg = parseWeight(raw.weight),
            vehicle = parseVehicle(raw.vehicle),
            material = parseExtraAccessoryMaterial(extra),
            color = parseExtraAccessoryColor(extra)
        )
    }
}