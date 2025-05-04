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
            Log.e(TAG, "readUser error :: ${e.message.toString()}")
            throw e
        }
    }

    override suspend fun createUser(user : FirebaseUser) {
        try {
            db.createUser(User(user.uid))
        } catch (e : Exception) {
            Log.e(TAG, "createUser error :: ${e.message.toString()}")
        }
    }

    override suspend fun deleteUser() {
        try {
            db.deleteUser(readUser())
        } catch (e : Exception) {
            Log.e(TAG, "deleteUser error :: ${e.message.toString()}")
        }
    }

    override suspend fun readAllFavoriteEvents(): List<BookmarkEvent> {
        return try {
            db.readAllFavorites(readUser())
        } catch (e : IllegalStateException) {
            Log.e(TAG, "readAllFavoriteEvents error :: ${e.message.toString()}")
            emptyList()
        }
    }

    override suspend fun favorite(bookmarkEvent: BookmarkEvent) {
        try {
            val user = readUser()
            if (db.isFavorite(user, bookmarkEvent.eventId))
                db.removeFavorite(user, bookmarkEvent.eventId)
            else
                db.createFavorite(user, bookmarkEvent)
        } catch (e : IllegalStateException) {
            Log.e(TAG, "favorite error :: ${e.message.toString()}")
        }
    }

    override suspend fun removeFavorite(eventId: String) {
        try {
            db.removeFavorite(readUser(), eventId)
        } catch (e : IllegalStateException) {
            Log.e(TAG, "removeFavorite error :: ${e.message.toString()}")
        }
    }

    override suspend fun isFavorite(eventId: String): Boolean {
        try {
            return db.isFavorite(readUser(), eventId)
        } catch (e : IllegalStateException) {
            Log.e(TAG, "isFavorite error :: ${e.message.toString()}")
            throw e
        }
    }
}