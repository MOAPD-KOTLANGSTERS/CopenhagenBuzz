package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.interfaces

import android.net.Uri

/**
 * Defines a service interface for managing event-related images in remote storage.
 */
interface IImageService {

    /**
     * Uploads an image for the specified event.
     *
     * @param eventId The ID of the event.
     * @param uri The URI of the image to upload.
     * @return A [Result] containing the download URL on success, or an error on failure.
     */
    suspend fun create(eventId: String, uri: Uri): Result<String>

    /**
     * Retrieves the download URL of the image for the specified event.
     *
     * @param eventId The ID of the event.
     * @return A [Result] containing the download URL on success, or an error on failure.
     */
    suspend fun read(eventId: String): Result<String>

    /**
     * Replaces the existing image for the specified event.
     *
     * @param eventId The ID of the event.
     * @param uri The URI of the new image to upload.
     * @return A [Result] containing the new download URL on success, or an error on failure.
     */
    suspend fun update(eventId: String, uri: Uri): Result<String>

    /**
     * Deletes the image associated with the specified event.
     *
     * @param eventId The ID of the event.
     * @return `true` if the deletion was successful, `false` otherwise.
     */
    suspend fun delete(eventId: String): Boolean
}
