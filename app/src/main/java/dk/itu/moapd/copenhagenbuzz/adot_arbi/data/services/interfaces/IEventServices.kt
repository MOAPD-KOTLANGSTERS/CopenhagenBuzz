package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.interfaces

import android.content.Context
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event

interface IEventServices {
    suspend fun createEvent(event: Event,  context: Context)
    suspend fun readAllEvents() : List<Event>
    suspend fun update(event: Event)
    suspend fun deleteEvent(event: Event)
    suspend fun readEventsFromId(eventId: String): Event?
    suspend fun exists(eventId: String) : Boolean
}