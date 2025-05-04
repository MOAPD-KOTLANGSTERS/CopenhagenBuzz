package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.interfaces

import com.google.firebase.auth.FirebaseUser
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.BookmarkEvent
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.User
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.EventRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.UserRepository

interface IUserServices {
    // user specific
    suspend fun createUser(user : FirebaseUser) : Unit
    suspend fun readUser() : User
    suspend fun deleteUser() : Unit

    // bookmark specific
    suspend fun createFavorite(bookmarkEvent: BookmarkEvent) : Unit
    suspend fun readAllFavoriteEvents(): List<BookmarkEvent>
    suspend fun deleteFavoriteEvent(eventId: String): Unit
}