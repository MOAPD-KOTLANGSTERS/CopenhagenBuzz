package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.interfaces

import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.EventRepository

interface IEventServices {
    suspend fun createEvent(event: Event) : Void
    suspend fun readAllEvents() : List<Event>
    suspend fun updateEvent(event: Event) : Void
    suspend fun deleteEvent(event: Event) : Void
}