package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.BookmarkEvent
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.User
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.BaseRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.UserRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.interfaces.IUserServices

/**
 * Provides an implementation of [IUserServices] for managing user accounts
 * and their bookmarked events in Firebase Realtime Database.
 */
object UserServices : IUserServices {

    private val TAG = BaseRepository::class.qualifiedName

    /**
     * Retrieves the currently authenticated user's profile data.
     *
     * @return The [User] object associated with the authenticated user.
     * @throws Exception if user data cannot be retrieved or the user is not authenticated.
     */
    override suspend fun readUser(): User {
        try {
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            val user = UserRepository.readUser(uid)
            return user ?: User()
        } catch (e: Exception) {
            Log.e(TAG, "readUser error :: ${e.message}")
            throw e
        }
    }

    /**
     * Creates a new user entry in the database for the given Firebase user.
     *
     * @param user The [FirebaseUser] to be saved.
     */
    override suspend fun createUser(user: FirebaseUser) {
        try {
            UserRepository.createUser(User(user.uid))
        } catch (e: Exception) {
            Log.e(TAG, "createUser error :: ${e.message}")
        }
    }

    /**
     * Deletes the currently authenticated user's profile from the database.
     */
    override suspend fun deleteUser() {
        try {
            UserRepository.deleteUser(readUser())
        } catch (e: Exception) {
            Log.e(TAG, "deleteUser error :: ${e.message}")
        }
    }

    /**
     * Retrieves all events the current user has marked as favorites.
     *
     * @return A list of [BookmarkEvent] objects, or an empty list if none are found.
     */
    override suspend fun readAllFavoriteEvents(): List<BookmarkEvent> {
        return try {
            UserRepository.readAllFavorites(readUser())
        } catch (e: IllegalStateException) {
            Log.e(TAG, "readAllFavoriteEvents error :: ${e.message}")
            emptyList()
        }
    }

    /**
     * Toggles a bookmarked event for the current user.
     * Adds it if not already bookmarked, removes it otherwise.
     *
     * @param bookmarkEvent The [BookmarkEvent] to toggle.
     */
    override suspend fun favorite(bookmarkEvent: BookmarkEvent) {
        try {
            val user = readUser()
            if (UserRepository.isFavorite(user, bookmarkEvent.eventId)) {
                UserRepository.removeFavorite(user, bookmarkEvent.eventId)
            } else {
                UserRepository.createFavorite(user, bookmarkEvent)
            }
        } catch (e: IllegalStateException) {
            Log.e(TAG, "favorite error :: ${e.message}")
        }
    }

    /**
     * Removes a specific event from the user's favorites.
     *
     * @param eventId The ID of the event to remove.
     */
    override suspend fun removeFavorite(eventId: String) {
        try {
            UserRepository.removeFavorite(readUser(), eventId)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "removeFavorite error :: ${e.message}")
        }
    }

    /**
     * Checks whether a given event is favorited by the current user.
     *
     * @param eventId The ID of the event to check.
     * @return `true` if the event is a favorite, `false` otherwise.
     * @throws IllegalStateException if the check fails unexpectedly.
     */
    override suspend fun isFavorite(eventId: String): Boolean {
        try {
            return UserRepository.isFavorite(readUser(), eventId)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "isFavorite error :: ${e.message}")
            throw e
        }
    }
}
