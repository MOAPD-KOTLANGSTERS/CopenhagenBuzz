package dk.itu.moapd.copenhagenbuzz.adot_arbi.model.services

import android.net.Uri
import android.util.Log
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.repository.ImageRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.services.interfaces.IImageService

/**
 * Provides an implementation of [IImageService] for managing image files
 * in Firebase Storage, including uploading, reading, updating, and deleting.
 */
object ImageService : IImageService {

    /**
     * Uploads a new image to Firebase Storage for the given event.
     *
     * @param eventId The ID of the event to associate the image with.
     * @param uri The URI of the image file to upload.
     * @return A [Result] containing the download URL on success, or an error on failure.
     */
    override suspend fun create(eventId: String, uri: Uri): Result<String> {
        return try {
            Result.success(ImageRepository.create(eventId, uri))
        } catch (e: Exception) {
            Log.e(ImageService::class.qualifiedName, e.message.toString())
            Result.failure(e)
        }
    }

    /**
     * Retrieves the download URL for the image associated with the given event ID.
     *
     * @param eventId The ID of the event.
     * @return A [Result] containing the download URL on success, or an error on failure.
     */
    override suspend fun read(eventId: String): Result<String> {
        return try {
            Result.success(ImageRepository.read(eventId))
        } catch (e: Exception) {
            Log.e(ImageService::class.qualifiedName, e.message.toString())
            Result.failure(e)
        }
    }

    /**
     * Updates (overwrites) the image for the given event with a new one.
     *
     * @param eventId The ID of the event.
     * @param uri The URI of the new image file.
     * @return A [Result] containing the updated download URL on success, or an error on failure.
     */
    override suspend fun update(eventId: String, uri: Uri): Result<String> {
        return try {
            Result.success(ImageRepository.update(eventId, uri))
        } catch (e: Exception) {
            Log.e(ImageService::class.qualifiedName, e.message.toString())
            Result.failure(e)
        }
    }

    /**
     * Deletes the image associated with the given event.
     *
     * @param eventId The ID of the event.
     * @return `true` if the image was successfully deleted, `false` otherwise.
     */
    override suspend fun delete(eventId: String): Boolean {
        return try {
            ImageRepository.delete(eventId)
            true
        } catch (e: Exception) {
            Log.e(ImageService::class.qualifiedName, e.message.toString())
            false
        }
    }
}
