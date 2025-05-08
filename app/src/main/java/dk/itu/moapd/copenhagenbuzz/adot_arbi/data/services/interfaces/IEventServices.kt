package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.interfaces

import android.content.Context
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event

/**
 * Defines a service interface for managing [Event] objects, including
 * creation, retrieval, updating, deletion, and existence checks.
 */
interface IEventServices {

    /**
     * Creates a new event and stores it in the data source.
     *
     * @param event The [Event] to be created.
     * @param context The [Context] required for operations involving local resources.
     */
    suspend fun createEvent(event: Event, context: Context) : Result<Event>

    /**
     * Retrieves all events from the data source.
     *
     * @return A list of [Event] objects, or an empty list if none exist.
     */
    suspend fun readAllEvents(): List<Event>

    /**
     * Updates an existing event in the data source.
     *
     * @param event The updated [Event] object.
     */
    suspend fun update(event: Event)

    /**
     * Deletes an event from the data source.
     *
     * @param event The [Event] to delete.
     */
    suspend fun deleteEvent(event: Event) : Result<Boolean>

    /**
     * Retrieves a specific event by its ID.
     *
     * @param eventId The unique identifier of the event.
     * @return The matching [Event] if found, or `null` if not.
     */
    suspend fun readEventsFromId(eventId: String): Event?

    /**
     * Checks whether an event with the given ID exists.
     *
     * @param eventId The ID of the event to check.
     * @return `true` if the event exists, `false` otherwise.
     */
    suspend fun exists(eventId: String): Boolean
}
