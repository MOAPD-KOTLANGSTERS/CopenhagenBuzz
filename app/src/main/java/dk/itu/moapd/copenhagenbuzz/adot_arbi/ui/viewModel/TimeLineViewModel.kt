package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.BookmarkEvent
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.EventRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.UserRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.UserServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TimeLineViewModel : ViewModel() {
    fun isFavorited(eventId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = UserServices().isFavorited(eventId)
            withContext(Dispatchers.Main) {
                onResult(result)
            }
        }
    }

    fun addFavorite(eventId: String, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val event = EventRepository().readEventsFromId(eventId)
            if (event != null) {
                UserServices().favorite(
                    BookmarkEvent(
                        eventId = eventId,
                        eventType = event.eventType,
                        eventDate = event.eventDate,
                        eventName = event.eventName,
                    )
                )
            }
            val result = UserServices().isFavorited(eventId)
            withContext(Dispatchers.Main) {
                onSuccess(result)
            }
        }
    }
}
