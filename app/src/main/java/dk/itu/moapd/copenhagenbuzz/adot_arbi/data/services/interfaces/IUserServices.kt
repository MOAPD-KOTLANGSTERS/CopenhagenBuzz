package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.interfaces

import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.User

interface IUserServices {
    // user specific
    suspend fun createUser() : Void
    suspend fun readUser() : User
    suspend fun deleteUser() : Void

    // bookmark specific
    suspend fun createFavorite(eventId: String) : Void
    suspend fun readAllFavoriteEvents(): List<Event>
    suspend fun deleteFavoriteEvent(eventId: String): Void
}