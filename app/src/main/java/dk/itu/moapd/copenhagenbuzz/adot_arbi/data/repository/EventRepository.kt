package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository

import android.util.Log
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import kotlinx.coroutines.tasks.await

class EventRepository : BaseRepository<Event>(Event::class.java,"event") {
    override suspend fun add(value: Event): Void {
        val ref = db.push()
        val id = ref.key ?: throw IllegalStateException("No key generated")
        val valueWithId = value.copy(id = id)
        return ref.setValue(valueWithId).await()

    }
}