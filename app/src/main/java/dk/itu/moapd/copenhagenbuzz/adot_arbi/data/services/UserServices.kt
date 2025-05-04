package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.BookmarkEvent
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.User
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.BaseRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.UserRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.interfaces.IUserServices

class UserServices(
    private val db : UserRepository = UserRepository()
) : IUserServices {
    companion object {
        private val TAG = BaseRepository::class.qualifiedName
    }

    override suspend fun readUser() : User {
        try {
            return db.readUser(FirebaseAuth.getInstance().currentUser?.uid!!)!!
        } catch (e: Exception) {
            Log.d(TAG, "readUser error :: ${e.message.toString()}")
            throw e
        }
    }

    override suspend fun createUser(user : FirebaseUser) {
        try {
            db.createUser(User(user.uid))
        } catch (e : Exception) {
            Log.d(TAG, "createUser error :: ${e.message.toString()}")
            throw e
        }
    }

    override suspend fun deleteUser() {
        try {
            db.deleteUser(readUser())
        } catch (e : Exception) {
            Log.d(TAG, "deleteUser error :: ${e.message.toString()}")
            throw e
        }
    }

    override suspend fun readAllFavoriteEvents(): List<BookmarkEvent> {
        try {
            return db.readAllFavorites(readUser())
        } catch (e : IllegalStateException) {
            Log.d(TAG, "readAllFavoriteEvents error :: ${e.message.toString()}")
            throw e
        }
    }

    override suspend fun favorite(bookmarkEvent: BookmarkEvent) {
        try {
            val user = readUser()
            if (db.isFavorited(user, bookmarkEvent.eventId))
                db.removeFavorite(user, bookmarkEvent.eventId)
            else
                db.createFavorite(user, bookmarkEvent)
        } catch (e : IllegalStateException) {
            Log.d(TAG, "favorite error :: ${e.message.toString()}")
            throw e
        }
    }

    override suspend fun isFavorited(eventId: String): Boolean {
        try {
            return db.isFavorited(readUser(), eventId)
        } catch (e : IllegalStateException) {
            Log.d(TAG, "isFavorited error :: ${e.message.toString()}")
            throw e
        }
    }
}