package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services

import android.util.Log
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.BaseRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.EventRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.UserServices.Companion
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.interfaces.IEventServices

class EventServices : IEventServices {
    companion object {
        private val TAG = EventServices::class.qualifiedName
    }
    private val db : EventRepository = EventRepository()
    
    override suspend fun createEvent(event: Event): Void {
        try {
            return db.add(event)
        } catch (e : Exception) {
            Log.d(TAG, "readUser error :: ${e.message.toString()}")
            throw e
        }
    }

    override suspend fun readAllEvents(): List<Event> {
        try {
            return db.getAll()
        } catch (e : Exception) {
            Log.d(TAG, "readAllEvents error :: ${e.message.toString()}")
            throw e
        }
    }

    override suspend fun updateEvent(event: Event): Void {
        try {
            return db.add(event)
        } catch (e : Exception) {
            Log.d(TAG, "updateEvent error :: ${e.message.toString()}")
            throw e
        }
    }

    override suspend fun deleteEvent(event: Event): Void {
        try {
            return db.delete(event.id!!)
        } catch (e : Exception) {
            Log.d(TAG, "deleteEvent error :: ${e.message.toString()}")
            throw e
        }
    }

}