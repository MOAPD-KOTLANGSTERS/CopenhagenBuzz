package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.BookmarkEvent
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.EventRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.UserServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TimeLineViewModel : ViewModel() {

    private val _selectedEvent = MutableLiveData<Event?>()
    val selectedEvent: MutableLiveData<Event?>
        get() { return _selectedEvent }

    init {
        selectedEvent.value = null
    }

    fun setEvent(event: Event? = null) {
        _selectedEvent.value = event
    }

    fun isFavorite(eventId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = UserServices.isFavorite(eventId)
            withContext(Dispatchers.Main) {
                onResult(result)
            }
        }
    }

    fun addFavorite(eventId: String, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val event = EventRepository.readEventsFromId(eventId)
            if (event != null) {
                UserServices.favorite(
                    BookmarkEvent(
                        eventId = eventId,
                        eventType = event.eventType,
                        eventDate = event.eventDate,
                        eventName = event.eventName,
                        url = event.eventPhotoURL,
                    )
                )
            }
            val result = UserServices.isFavorite(eventId)
            withContext(Dispatchers.Main) {
                onSuccess(result)
            }
        }
    }

}
