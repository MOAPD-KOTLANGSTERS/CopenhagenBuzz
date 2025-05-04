package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.interfaces

import com.google.firebase.auth.FirebaseUser
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.BookmarkEvent
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.User

interface IUserServices {
    // user specific
    suspend fun createUser(user : FirebaseUser)
    suspend fun readUser() : User
    suspend fun deleteUser()

    // bookmark specific
    suspend fun favorite(bookmarkEvent: BookmarkEvent)
    suspend fun removeFavorite(eventId: String)
    suspend fun isFavorite(eventId: String) : Boolean
    suspend fun readAllFavoriteEvents(): List<BookmarkEvent>
}