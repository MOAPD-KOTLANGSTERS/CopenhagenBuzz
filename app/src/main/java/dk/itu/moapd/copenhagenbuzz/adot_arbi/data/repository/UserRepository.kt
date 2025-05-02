package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository

import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.DummyModel
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import kotlinx.coroutines.tasks.await

class UserRepository : BaseRepository<DummyModel>(DummyModel::class.java,"user") {
    private fun getUid(): String =
        FirebaseAuth.getInstance().currentUser?.uid ?: throw IllegalStateException("User not logged in")

    suspend fun createUser(): Void =
        db.push()
            .setValue(getUid())
            .await()

    suspend fun deleteUser(): Void =
        db.child(getUid())
            .removeValue()
            .await()

    suspend fun createFavorite(eventId: String): Void =
        db.child(getUid())
            .child("favorites")
            .push()
            .setValue(eventId)
            .await()

    suspend fun readAllFavorites() =
        db.child(getUid())
            .child("favorites")
            .get()
            .await()
            .children
            .mapNotNull { it.getValue(Event::class.java) }

    suspend fun removeFavorite(eventId: String): Void =
        db.child(getUid())
            .child("favorites")
            .child(eventId)
            .removeValue()
            .await()
}