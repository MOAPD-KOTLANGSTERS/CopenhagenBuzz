package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.BaseRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.UserRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.interfaces.IUserServices
import dk.itu.moapd.copenhagenbuzz.adot_arbi.util.Coroutine

class UserServices : IUserServices {
    companion object {
        private val TAG = BaseRepository::class.qualifiedName
    }
    private val db : UserRepository = UserRepository()
    override fun createUser() {
        try {
            Coroutine.suspendLaunch { db.createUser() }
        } catch (e : IllegalStateException) {
            Log.d(TAG, "createUser error :: ${e.message.toString()}")
        }
    }

    override fun deleteUser() {
        try {
            Coroutine.suspendLaunch { db.deleteUser() }
        } catch (e : IllegalStateException) {
            Log.d(TAG, "deleteUser error :: ${e.message.toString()}")
        }
    }

    override fun createFavorite(eventId: String) {
        try {
            Coroutine.suspendLaunch { db.createFavorite(eventId) }
        } catch (e : IllegalStateException) {
            Log.d(TAG, "createFavorite error :: ${e.message.toString()}")
        }
    }

    override fun readAllFavoriteEvents() {
        try {
            Coroutine.suspendLaunch { db.readAllFavorites() }
        } catch (e : IllegalStateException) {

        }
    }

    override fun deleteFavoriteEvent(eventId: String) {
        try {

        } catch (e : IllegalStateException) {

        }
    }
}