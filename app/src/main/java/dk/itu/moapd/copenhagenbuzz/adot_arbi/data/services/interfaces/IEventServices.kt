package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.interfaces

import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event

interface IEventServices {
    suspend fun createEvent(event: Event)
    suspend fun readAllEvents() : List<Event>
    suspend fun update(event: Event)
    suspend fun deleteEvent(event: Event)
    suspend fun readEventsFromId(eventId: String): Event?
    suspend fun exists(eventId: String) : Boolean
}