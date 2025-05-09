package dk.itu.moapd.copenhagenbuzz.adot_arbi.model.dto

/**
 * Represents a user in the system.
 *
 * @property uuid The unique identifier of the user.
 * @property favorites A map of the user's bookmarked events, keyed by event ID.
 */
data class User(
    val uuid: String = "",
    val favorites: Map<String, BookmarkEvent> = emptyMap(),
)
