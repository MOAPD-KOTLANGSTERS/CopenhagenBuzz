package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services

import android.net.Uri
import android.util.Log
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.ImageRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.interfaces.IImageService

object ImageService : IImageService {
    override suspend fun create(eventId: String, uri: Uri): Result<String> {
        return try {
            Result.success(ImageRepository.create(eventId, uri))
        } catch (e : Exception) {
            Log.e(ImageService::class.qualifiedName, e.message.toString())
            Result.failure(e)
        }
    }

    override suspend fun read(eventId: String): Result<String> {
        return try {
            Result.success(ImageRepository.read(eventId))
        } catch (e : Exception) {
            Log.e(ImageService::class.qualifiedName, e.message.toString())
            Result.failure(e)
        }
    }

    override suspend fun update(eventId: String, uri: Uri): Result<String> {
        return try {
            Result.success(ImageRepository.update(eventId, uri))
        } catch (e : Exception) {
            Log.e(ImageService::class.qualifiedName, e.message.toString())
            Result.failure(e)
        }
    }

    override suspend fun delete(eventId: String): Boolean {
        return try {
            ImageRepository.delete(eventId)
            true
        } catch (e : Exception) {
            Log.e(ImageService::class.qualifiedName, e.message.toString())
            false
        }
    }

}