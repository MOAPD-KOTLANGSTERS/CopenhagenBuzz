package dk.itu.moapd.copenhagenbuzz.adot_arbi.model.repository

import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.dto.Event
import kotlinx.coroutines.tasks.await

/**
 * A repository for managing [Event] objects in Firebase Realtime Database.
 * Inherits generic CRUD operations from [BaseRepository] and provides
 * event-specific overrides and extensions.
 */
object EventRepository : BaseRepository<Event>(Event::class.java, "event") {

    /**
     * Adds a new event to the database, automatically assigning a unique ID
     * to the event and storing it as part of the object.
     *
     * @param value The [Event] to be added.
     * @throws IllegalStateException if a Firebase key could not be generated.
     */
    override suspend fun add(value: Event) : Event {
        val ref = db.push()
        val id = ref.key ?: throw IllegalStateException("No key generated")
        val valueWithId = value.copy(id = id)
        ref.setValue(valueWithId).await()
        return ref.get().await().getValue(Event::class.java)!!
    }

    /**
     * Retrieves a single [Event] object from the database using its ID.
     *
     * @param eventId The ID of the event to retrieve.
     * @return The [Event] if found, or `null` if not found.
     */
    suspend fun readEventsFromId(eventId: String): Event? {
        return db.child(eventId).get().await().getValue(Event::class.java)
    }
}
