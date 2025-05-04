package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.EventLocation
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.EventRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.interfaces.IEventServices
import getAddressFromCoordinates
import getCoordinatesFromAddress


class EventServices(
    private val context: Context,
    private val db: EventRepository = EventRepository()
) : IEventServices {

    companion object {
        private val TAG = EventServices::class.qualifiedName
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override suspend fun createEvent(event: Event) {

        try {
            val addressName = event.eventLocation.address
            val coordinates = getCoordinatesFromAddress(context, addressName)
            val updatedLocation = coordinates?.let { (lat, lng) ->
                val resolvedAddress = getAddressFromCoordinates(context, lat, lng) ?: addressName
                EventLocation(lat, lng, resolvedAddress)
            } ?: EventLocation(0.0, 0.0, addressName)
            db.add(event.copy(eventLocation = updatedLocation))

        } catch (e: Exception) {
            Log.e(TAG,"createEvent error: ${e.message}")
        }
    }

    override suspend fun readAllEvents(): List<Event> {
        return try {
            db.getAll()
        } catch (e: Exception) {
            Log.e(TAG, "readAllEvents error: ${e.message}")
            emptyList()
        }
    }

    override suspend fun deleteEvent(event: Event) {
        try {
            if (exists(event.id!!))
                db.delete(event.id!!)
        } catch (e: Exception) {
            Log.e(TAG, "deleteEvent error: ${e.message}")
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

    override suspend fun update(event: Event) {
        try {
            db.update(event.id!!, event)
        } catch (e: Exception) {
            Log.e(TAG, "update error :: ${e.message.toString()}")
        }
    }

    override suspend fun exists(eventId: String) : Boolean {
        return try {
            db.exists(eventId)
        } catch (e: Exception) {
            Log.e(TAG, "verifyEvent error :: ${e.message.toString()}")
            false
        }
    }
}

