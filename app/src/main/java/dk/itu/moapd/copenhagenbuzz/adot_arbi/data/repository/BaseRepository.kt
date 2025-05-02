package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dk.itu.moapd.copenhagenbuzz.adot_arbi.util.DotenvManager
import kotlinx.coroutines.tasks.await

/**
 * Abstract generic repository with generic methods
 */
abstract class BaseRepository<T : Any>(private val clazz: Class<T>, private val child: String) {
    companion object {
        private val TAG = BaseRepository::class.qualifiedName
    }

    /**
     * Thread-safe lazy singleton implementation
     */
    protected val db : DatabaseReference by lazy {
        Firebase.database(DotenvManager.DATABASE_URL)
            .reference
            .child("copenhagen_buzz/$child")
            .also { it.keepSynced(true) }
    }

    /**
     * Add a value
     */
    open suspend fun add(value: T) = db.push().setValue(value).await()

    /**
     * Delete a value
     */
    suspend fun delete(id: String) = db.child(id).removeValue().await()

    /**
     * Get a T
     */
    suspend fun getById(id: String) = db.child(id).get().await().getValue(clazz)

    /**
     * Get all T as a list
     */
    suspend fun getAll() = db.get().await().children.mapNotNull { it.getValue(clazz) }

}