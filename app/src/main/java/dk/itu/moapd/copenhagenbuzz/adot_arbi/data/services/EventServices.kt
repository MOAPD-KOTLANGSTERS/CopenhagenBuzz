package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services

import android.util.Log
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.BaseRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.EventRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.UserServices.Companion
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.interfaces.IEventServices

class EventServices (
    private val db : EventRepository = EventRepository()
) : IEventServices {
    companion object {
        private val TAG = EventServices::class.qualifiedName
    }

    override suspend fun createEvent(event: Event) {
        try {
            db.add(event)
        } catch (e : Exception) {
            Log.e(TAG, "readUser error :: ${e.message.toString()}")
        }
    }

    override suspend fun readAllEvents(): List<Event> {
        return try {
            db.getAll()
        } catch (e : Exception) {
            Log.e(TAG, "readAllEvents error :: ${e.message.toString()}")
            emptyList()
        }
    }

    override suspend fun updateEvent(event: Event) {
        try {
            db.add(event)
        } catch (e : Exception) {
            Log.e(TAG, "updateEvent error :: ${e.message.toString()}")
        }
    }

    override suspend fun deleteEvent(event: Event) {
        try {
            db.delete(event.id!!)
        } catch (e : Exception) {
            Log.e(TAG, "deleteEvent error :: ${e.message.toString()}")
        }
    }

    override suspend fun readEventsFromId(eventId: String): Event? {
        return try {
             db.readEventsFromId(eventId)
        } catch (e : Exception) {
            Log.e(TAG, "deleteEvent error :: ${e.message.toString()}")
            null
        }
    }
}