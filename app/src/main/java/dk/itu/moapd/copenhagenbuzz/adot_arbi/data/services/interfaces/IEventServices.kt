package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.interfaces

import android.content.Context
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event

/**
 * Service interface for managing [Event] objects, including creation,
 * retrieval, updating, deletion, and existence checks.
 */
interface IEventServices {

    /**
     * Creates and stores a new [Event] in the data source.
     *
     * @param event The [Event] to create.
     * @param context The [Context] used for operations involving local resources.
     * @return A [Result] containing the created [Event], or a failure if the operation fails.
     */
    suspend fun createEvent(event: Event, context: Context): Result<Event>

    /**
     * Retrieves all events from the data source.
     *
     * @return A list of all [Event] objects, or an empty list if none are found.
     */
    suspend fun readAllEvents(): List<Event>

    /**
     * Updates an existing [Event] in the data source.
     *
     * @param event The [Event] object containing updated data.
     */
    suspend fun update(event: Event)

    /**
     * Deletes the specified [Event] from the data source.
     *
     * @param event The [Event] to delete.
     * @return A [Result] indicating success (`true`) or failure.
     */
    suspend fun deleteEvent(event: Event): Result<Boolean>

    /**
     * Retrieves a specific [Event] by its ID.
     *
     * @param eventId The unique identifier of the event.
     * @return The matching [Event] if found, or `null` if it does not exist.
     */
    suspend fun readEventsFromId(eventId: String): Event?

    /**
     * Checks whether an event with the specified ID exists in the data source.
     *
     * @param eventId The unique identifier of the event to check.
     * @return `true` if the event exists, `false` otherwise.
     */
    suspend fun exists(eventId: String): Boolean
}
