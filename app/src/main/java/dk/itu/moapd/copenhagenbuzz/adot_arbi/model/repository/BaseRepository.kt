package dk.itu.moapd.copenhagenbuzz.adot_arbi.model.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dk.itu.moapd.copenhagenbuzz.adot_arbi.util.DotenvManager
import kotlinx.coroutines.tasks.await

/**
 * An abstract generic repository that provides basic CRUD operations
 * for Firebase Realtime Database entries of type [T].
 *
 * @param T The type of object managed by the repository.
 * @param clazz The class type of [T] for deserialization.
 * @param child The child node under the root path where objects are stored.
 */
abstract class BaseRepository<T : Any>(private val clazz: Class<T>, private val child: String) {

    companion object {
        private val TAG = BaseRepository::class.qualifiedName
    }

    /**
     * The root path under which all data is stored.
     */
    protected val path: String
        get() = "copenhagen_buzz/"

    /**
     * Lazily initialized thread-safe reference to the Firebase database path
     * corresponding to the specified child node.
     */
    val db: DatabaseReference by lazy {
        Firebase.database(DotenvManager.RT_DATABASE_URL)
            .reference
            .child("$path/$child")
            .also { it.keepSynced(true) }
    }

    /**
     * Adds a new value to the database under a generated unique ID.
     *
     * @param value The object to store.
     */
    open suspend fun add(value: T): T {
        val newRef = db.push()
        newRef.setValue(value).await()
        return newRef.get().await().getValue(clazz)!!
    }


    /**
     * Deletes the value with the specified ID from the database.
     *
     * @param id The ID of the object to delete.
     */
    suspend fun delete(id: String) {
        db.child(id).removeValue().await()
    }

    /**
     * Updates the value at the specified ID with a new object.
     *
     * @param id The ID of the object to update.
     * @param value The new object value.
     */
    suspend fun update(id: String, value: T) {
        db.child(id).setValue(value).await()
    }

    /**
     * Retrieves a single object by its ID.
     *
     * @param id The ID of the object to retrieve.
     * @return The object if found, or `null` if not present.
     */
    suspend fun getById(id: String): T? {
        return db.child(id).get().await().getValue(clazz)
    }

    /**
     * Retrieves all objects of type [T] under the current node.
     *
     * @return A list of all objects found, or an empty list if none exist.
     */
    suspend fun getAll(): List<T> {
        return db.get().await().children.mapNotNull { it.getValue(clazz) }
    }

    /**
     * Checks whether an object with the given ID exists in the database.
     *
     * @param id The ID to check for existence.
     * @return `true` if the object exists, `false` otherwise.
     */
    suspend fun exists(id: String): Boolean {
        return db.child(id).get().await().exists()
    }
}
