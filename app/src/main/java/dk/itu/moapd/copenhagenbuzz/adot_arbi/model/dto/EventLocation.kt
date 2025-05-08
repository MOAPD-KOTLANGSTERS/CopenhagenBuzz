package dk.itu.moapd.copenhagenbuzz.adot_arbi.model.dto

/**
 * Represents the geographical location of an event.
 *
 * @property lat The latitude coordinate of the event location.
 * @property long The longitude coordinate of the event location.
 * @property address The human-readable address of the location.
 */
data class EventLocation(
    val lat: Double = 0.0,
    val long: Double = 0.0,
    val address: String = "",
) {
    override fun toString() = "$lat, $long, $address"
}
