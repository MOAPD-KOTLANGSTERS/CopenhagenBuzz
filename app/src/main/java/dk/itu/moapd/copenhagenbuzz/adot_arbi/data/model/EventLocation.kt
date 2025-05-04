package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model

data class EventLocation(
    val lat : Double = 0.0,
    val long : Double = 0.0,
    val address: String = "",
) {

    override fun toString() = "$lat, $long, $address"
}
