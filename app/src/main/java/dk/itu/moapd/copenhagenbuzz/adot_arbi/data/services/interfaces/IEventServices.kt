package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.interfaces

import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event

interface IEventServices {
    suspend fun createEvent(event: Event) : Unit
    suspend fun readAllEvents() : List<Event>
    suspend fun updateEvent(event: Event) : Unit
    suspend fun deleteEvent(event: Event) : Unit
    suspend fun readEventsFromId(eventId: String): Event?
}