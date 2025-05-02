package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.getValue
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.DummyModel
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.User
import kotlinx.coroutines.tasks.await

class UserRepository : BaseRepository<User>(User::class.java,"user") {
    suspend fun createUser(user: User): Void =
        db.push()
            .setValue(user.uuid)
            .await()

    suspend fun readUser(userId: String?) : User? =
        db.child(userId ?: throw IllegalStateException())
            .get()
            .await()
            .getValue(User::class.java)

    suspend fun deleteUser(user: User): Void =
        db.child(user.uuid)
            .removeValue()
            .await()

    suspend fun createFavorite(user : User, eventId: String): Void =
        db.child(user.uuid)
            .child("favorites")
            .push()
            .setValue(eventId)
            .await()

    suspend fun readAllFavorites(user: User) =
        db.child(user.uuid)
            .child("favorites")
            .get()
            .await()
            .children
            .mapNotNull { it.getValue(Event::class.java) }

    suspend fun removeFavorite(user: User, eventId: String): Void =
        db.child(user.uuid)
            .child("favorites")
            .child(eventId)
            .removeValue()
            .await()
}