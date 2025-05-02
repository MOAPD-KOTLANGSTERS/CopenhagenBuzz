package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model

data class EventLocation(
    private val lat: Double?,
    private val long: Double?,
    private val address: String
) {
    init {
        require(address.isNotEmpty()) { throw IllegalArgumentException("EventLocation must not be empty!")}
    }
    override fun toString() = address
}
