package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository

import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.BookmarkEvent
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.User
import kotlinx.coroutines.tasks.await

/**
 * A repository for managing [User] data in Firebase Realtime Database,
 * including user profiles and favorite events.
 */
object UserRepository : BaseRepository<User>(User::class.java, "user") {

    /**
     * Creates or updates a user in the database if the user already exists.
     *
     * @param user The [User] object to be saved.
     */
    suspend fun createUser(user: User) {
        if (!exists(user.uuid)) {
            db.child(user.uuid)
                .setValue(user)
                .await()
        }
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The [User] object if found, or `null` if not found.
     */
    suspend fun readUser(userId: String): User? {
        return db.child(userId)
            .get()
            .await()
            .getValue(User::class.java)
    }

    /**
     * Deletes a user from the database.
     *
     * @param user The [User] to be deleted.
     */
    suspend fun deleteUser(user: User) {
        db.child(user.uuid)
            .removeValue()
            .await()
    }

    /**
     * Adds an event to the user's list of favorite events.
     *
     * @param user The [User] who is favoriting the event.
     * @param event The [BookmarkEvent] to be added to favorites.
     */
    suspend fun createFavorite(user: User, event: BookmarkEvent) {
        db.child(user.uuid)
            .child("favorites")
            .child(event.eventId)
            .setValue(event)
            .await()
    }

    /**
     * Retrieves all events the user has marked as favorites.
     *
     * @param user The [User] whose favorites to retrieve.
     * @return A list of [BookmarkEvent] objects, or an empty list if none exist.
     */
    suspend fun readAllFavorites(user: User): List<BookmarkEvent> {
        return db.child(user.uuid)
            .child("favorites")
            .get()
            .await()
            .children
            .mapNotNull { it.getValue(BookmarkEvent::class.java) }
    }

    /**
     * Removes an event from the user's favorites.
     *
     * @param user The [User] who wants to remove the favorite.
     * @param eventId The ID of the event to remove.
     */
    suspend fun removeFavorite(user: User, eventId: String) {
        db.child(user.uuid)
            .child("favorites")
            .child(eventId)
            .removeValue()
            .await()
    }

    /**
     * Checks whether a given event is marked as a favorite by the user.
     *
     * @param user The [User] to check.
     * @param eventId The ID of the event to look for in favorites.
     * @return `true` if the event is a favorite, `false` otherwise.
     */
    suspend fun isFavorite(user: User, eventId: String): Boolean {
        return db.child(user.uuid)
            .child("favorites")
            .child(eventId)
            .get()
            .await()
            .exists()
    }
}
