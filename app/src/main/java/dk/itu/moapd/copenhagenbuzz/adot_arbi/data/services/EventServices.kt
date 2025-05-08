package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services

import android.content.Context
import android.util.Log
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.EventLocation
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.EventRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.interfaces.IEventServices
import getAddressFromCoordinates
import getCoordinatesFromAddress

/**
 * Implementation of [IEventServices] for managing [Event] objects.
 * Supports CRUD operations and geolocation resolution.
 */
object EventServices : IEventServices {

    private val TAG = EventServices::class.qualifiedName

    /**
     * Creates a new [Event] with resolved geolocation based on the provided address.
     *
     * @param event The [Event] to create.
     * @param context The [Context] used for geocoding services.
     * @return A [Result] containing the created [Event], or a failure if an error occurs.
     */
    override suspend fun createEvent(event: Event, context: Context): Result<Event> {
        return try {
            val address = event.eventLocation.address
            val coordinates = getCoordinatesFromAddress(context, address)

            val resolvedLocation = coordinates?.let { (lat, lng) ->
                val resolvedAddress = getAddressFromCoordinates(context, lat, lng) ?: address
                EventLocation(lat, lng, resolvedAddress)
            } ?: EventLocation(0.0, 0.0, address)

            Result.success(EventRepository.add(event.copy(eventLocation = resolvedLocation)))
        } catch (e: Exception) {
            Log.e(TAG, "createEvent failed: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Retrieves all [Event] objects from the repository.
     *
     * @return A list of all events, or an empty list if retrieval fails.
     */
    override suspend fun readAllEvents(): List<Event> {
        return try {
            EventRepository.getAll()
        } catch (e: Exception) {
            Log.e(TAG, "readAllEvents failed: ${e.message}", e)
            emptyList()
        }
    }

    /**
     * Deletes the given [Event] if it exists.
     *
     * @param event The [Event] to delete.
     * @return A [Result] indicating success (`true`) or a failure if not found or deletion fails.
     */
    override suspend fun deleteEvent(event: Event): Result<Boolean> {
        val eventId = event.id
        if (eventId == null) {
            Log.w(TAG, "deleteEvent failed: Event ID is null.")
            return Result.failure(NullPointerException("deleteEvent failed: event ID is null."))
        }

        return try {
            if (exists(eventId)) {
                EventRepository.delete(eventId)
                Result.success(true)
            } else {
                Log.w(TAG, "deleteEvent skipped: No event with ID $eventId found.")
                Result.failure(IllegalStateException("deleteEvent skipped: event with ID $eventId does not exist."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "deleteEvent failed: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Retrieves an [Event] by its ID.
     *
     * @param eventId The unique identifier of the event.
     * @return The [Event] if found, or `null` if not found or an error occurs.
     */
    override suspend fun readEventsFromId(eventId: String): Event? {
        return try {
            EventRepository.readEventsFromId(eventId)
        } catch (e: Exception) {
            Log.e(TAG, "readEventsFromId failed: ${e.message}", e)
            null
        }
    }

    /**
     * Updates the specified [Event] in the repository.
     *
     * @param event The [Event] with updated values.
     */
    override suspend fun update(event: Event) {
        val eventId = event.id
        if (eventId == null) {
            Log.w(TAG, "update failed: Event ID is null.")
            return
        }

        try {
            EventRepository.update(eventId, event)
        } catch (e: Exception) {
            Log.e(TAG, "update failed: ${e.message}", e)
        }
    }

    /**
     * Checks if an [Event] with the specified ID exists.
     *
     * @param eventId The unique identifier to check.
     * @return `true` if the event exists, or `false` if not found or an error occurs.
     */
    override suspend fun exists(eventId: String): Boolean {
        return try {
            EventRepository.exists(eventId)
        } catch (e: Exception) {
            Log.e(TAG, "exists check failed: ${e.message}", e)
            false
        }
    }
}
