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
 * Provides an implementation of [IEventServices] for managing [Event] objects.
 * Handles CRUD operations and resolves geolocation from addresses and vice versa.
 */
object EventServices : IEventServices {

    private val TAG = EventServices::class.qualifiedName

    /**
     * Creates a new event with location data resolved from the provided address.
     *
     * @param event The [Event] to create.
     * @param context The [Context] used for geocoding services.
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
            Log.e(TAG, "createEvent error: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Retrieves all events from the repository.
     *
     * @return A list of all [Event] objects, or an empty list if retrieval fails.
     */
    override suspend fun readAllEvents(): List<Event> {
        return try {
            EventRepository.getAll()
        } catch (e: Exception) {
            Log.e(TAG, "readAllEvents error: ${e.message}", e)
            emptyList()
        }
    }

    /**
     * Deletes the specified event if it exists.
     *
     * @param event The [Event] to delete.
     */
    override suspend fun deleteEvent(event: Event) {
        val eventId = event.id
        if (eventId == null) {
            Log.w(TAG, "deleteEvent failed: event ID is null.")
            return
        }

        try {
            if (exists(eventId)) {
                EventRepository.delete(eventId)
            } else {
                Log.w(TAG, "deleteEvent skipped: event with ID $eventId does not exist.")
            }
        } catch (e: Exception) {
            Log.e(TAG, "deleteEvent error: ${e.message}", e)
        }
    }

    /**
     * Retrieves a specific event by its ID.
     *
     * @param eventId The ID of the event to retrieve.
     * @return The matching [Event] if found, or `null` if not found or an error occurs.
     */
    override suspend fun readEventsFromId(eventId: String): Event? {
        return try {
            EventRepository.readEventsFromId(eventId)
        } catch (e: Exception) {
            Log.e(TAG, "readEventsFromId error: ${e.message}", e)
            null
        }
    }

    /**
     * Updates an existing event.
     *
     * @param event The updated [Event] object.
     */
    override suspend fun update(event: Event) {
        val eventId = event.id
        if (eventId == null) {
            Log.w(TAG, "update failed: event ID is null.")
            return
        }

        try {
            EventRepository.update(eventId, event)
        } catch (e: Exception) {
            Log.e(TAG, "update error: ${e.message}", e)
        }
    }

    /**
     * Checks whether an event with the given ID exists.
     *
     * @param eventId The ID to check.
     * @return `true` if the event exists, `false` otherwise or if an error occurs.
     */
    override suspend fun exists(eventId: String): Boolean {
        return try {
            EventRepository.exists(eventId)
        } catch (e: Exception) {
            Log.e(TAG, "exists error: ${e.message}", e)
            false
        }
    }
}
