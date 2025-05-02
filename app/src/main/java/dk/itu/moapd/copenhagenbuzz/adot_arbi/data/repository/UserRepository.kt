package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository

import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.DummyModel
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import kotlinx.coroutines.tasks.await

class UserRepository : BaseRepository<DummyModel>(DummyModel::class.java,"user") {
    suspend fun createUser() =
        db.push().setValue(
            FirebaseAuth
                .getInstance()
                .currentUser?.uid
                ?: run { throw IllegalStateException() }
        )

    suspend fun removeUser() =
        db.child(
            FirebaseAuth
                .getInstance()
                .currentUser?.uid
                ?: run { throw IllegalStateException() }
        ).removeValue()

    suspend fun createFavorite(eventId: String) =
        db.child(
            FirebaseAuth
                .getInstance()
                .currentUser?.uid
                ?: run { throw IllegalStateException() }
        ).push().setValue(eventId)

    suspend fun readAllFavorites() = db.child(
        FirebaseAuth
            .getInstance()
            .currentUser?.uid
            ?: run { throw IllegalStateException() }
    ).get().await().children.mapNotNull { it.getValue(Event::class.java) }
}