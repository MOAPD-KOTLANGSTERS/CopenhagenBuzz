package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository

import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.User
import kotlinx.coroutines.tasks.await

class UserRepository : BaseRepository<User>(User::class.java,"user") {

    private suspend fun isExists(userId: String): Boolean {
        val snapshot = db.child(userId).get().await()
        return snapshot.exists()
    }

    suspend fun createUser(user: User) {
        if (!isExists(user.uuid)) {
            db.child(user.uuid)
                .setValue(user)
                .await()
        }
    }

    suspend fun readUser(userId: String) : User? {
        return db.child(userId)
            .get()
            .await()
            .getValue(User::class.java)
    }


    suspend fun deleteUser(user: User) {
        db.child(user.uuid)
            .removeValue()
            .await()
    }


    suspend fun createFavorite(user : User, eventId: String) {
        db.child(user.uuid)
            .child("favorites")
            .child(eventId)
            .setValue(true)
            .await()
    }


    suspend fun readAllFavorites(user: User): List<String> {
        return db.child(user.uuid)
            .child("favorites")
            .get()
            .await()
            .children
            .mapNotNull { it.key }
    }

    suspend fun removeFavorite(user: User, eventId: String) {
        db.child(user.uuid)
            .child("favorites")
            .child(eventId)
            .removeValue()
            .await()
    }

}