package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.interfaces

import com.google.firebase.auth.FirebaseUser
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.BookmarkEvent
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.User

/**
 * Defines a service interface for managing user accounts and their bookmarked events.
 */
interface IUserServices {

    /**
     * Creates or initializes a user profile in the data source based on a Firebase user.
     *
     * @param user The [FirebaseUser] from Firebase Authentication.
     */
    suspend fun createUser(user: FirebaseUser)

    /**
     * Retrieves the currently logged-in user's profile data.
     *
     * @return The current [User] object.
     */
    suspend fun readUser(): User

    /**
     * Deletes the current user's profile from the data source.
     */
    suspend fun deleteUser()

    /**
     * Marks an event as a favorite for the current user.
     *
     * @param bookmarkEvent The [BookmarkEvent] to be added to the user's favorites.
     */
    suspend fun favorite(bookmarkEvent: BookmarkEvent)

    /**
     * Removes an event from the user's favorites.
     *
     * @param eventId The ID of the event to remove from favorites.
     */
    suspend fun removeFavorite(eventId: String)

    /**
     * Checks whether the specified event is favorited by the current user.
     *
     * @param eventId The ID of the event to check.
     * @return `true` if the event is a favorite, `false` otherwise.
     */
    suspend fun isFavorite(eventId: String): Boolean

    /**
     * Retrieves all events the user has marked as favorites.
     *
     * @return A list of [BookmarkEvent] objects.
     */
    suspend fun readAllFavoriteEvents(): List<BookmarkEvent>
}
