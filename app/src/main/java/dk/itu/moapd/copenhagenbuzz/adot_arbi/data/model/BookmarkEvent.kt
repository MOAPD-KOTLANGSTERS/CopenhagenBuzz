package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model

/**
 * Represents a bookmarked event.
 *
 * @property eventId The unique identifier of the event.
 * @property eventType The category or type of the event.
 * @property eventName The name/title of the event.
 * @property url An optional URL pointing to the event's image in the [ImageRepository].
 */
class BookmarkEvent(
    val eventId: String = "",
    val eventType: String = "",
    val eventName: String = "",
    val url: String? = null
)
