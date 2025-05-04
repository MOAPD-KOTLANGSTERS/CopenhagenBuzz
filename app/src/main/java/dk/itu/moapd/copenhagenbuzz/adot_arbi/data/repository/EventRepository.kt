package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository

import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import kotlinx.coroutines.tasks.await

class EventRepository : BaseRepository<Event>(Event::class.java,"event") {
    override suspend fun add(value: Event) {
        val ref = db.push()
        val id = ref.key ?: throw IllegalStateException("No key generated")
        val valueWithId = value.copy(id = id)
        ref.setValue(valueWithId).await()
    }

    suspend fun readEventsFromId(eventId: String): Event? {
        return db.child(eventId).get().await().getValue(Event::class.java)
    }
}