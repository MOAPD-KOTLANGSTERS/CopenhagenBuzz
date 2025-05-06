package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dk.itu.moapd.copenhagenbuzz.adot_arbi.util.DotenvManager
import kotlinx.coroutines.tasks.await

/**
 * A repository that provides access to Firebase Storage for image files related to events.
 *
 * This object should only be used within services that are responsible for handling image uploads,
 * downloads, updates, and deletions in the "copenhagen_buzz/events" storage path.
 */
object ImageRepository {

    /**
     * A thread-safe reference to the Firebase Storage location for event images.
     */
    private val storage: StorageReference by lazy {
        Firebase.storage(DotenvManager.STORAGE_DATABASE_URL)
            .getReference("copenhagen_buzz/events")
    }

    /**
     * Uploads an image to Firebase Storage at the path: `eventId/image.jpg`.
     *
     * If a file already exists at this path, it will be overwritten.
     *
     * @param eventId The unique identifier for the event.
     * @param uri The URI of the image file to upload.
     * @return A download URL of the uploaded image.
     */
    suspend fun create(eventId: String, uri: Uri): String {
        val ref = storage.child("$eventId/image.jpg")
        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }

    /**
     * Retrieves the download URL of the image stored under the given event ID.
     *
     * @param eventId The unique identifier for the event.
     * @return The download URL of the stored image.
     */
    suspend fun read(eventId: String): String {
        return storage.child("$eventId/image.jpg").downloadUrl.await().toString()
    }

    /**
     * Uploads or replaces an image at the specified path.
     *
     * This method behaves the same as [create], as Firebase Storage automatically
     * overwrites files at the same path.
     *
     * @param eventId The unique identifier for the event.
     * @param uri The URI of the image file to upload or replace.
     * @return A download URL of the uploaded image.
     */
    suspend fun update(eventId: String, uri: Uri): String = create(eventId, uri)

    /**
     * Deletes the image associated with the specified event ID from Firebase Storage.
     *
     * @param eventId The unique identifier for the event.
     */
    suspend fun delete(eventId: String) {
        storage.child("$eventId/image.jpg").delete().await()
    }
}
