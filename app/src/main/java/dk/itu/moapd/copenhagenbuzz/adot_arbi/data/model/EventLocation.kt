package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model

data class EventLocation(
    val lat : Double = 0.0,
    val long : Double = 0.0,
    val address: String
) {

    init {
        require(address.isNotEmpty()) { throw IllegalArgumentException("EventLocation must not be empty!")}
    }
    override fun toString() = "$lat, $long, $address"
}
