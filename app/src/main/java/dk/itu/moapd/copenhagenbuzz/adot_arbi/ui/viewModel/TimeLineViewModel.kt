package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.BookmarkEvent
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.EventRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.UserServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TimeLineViewModel : ViewModel() {

    fun addFavorite(eventId: String) {
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
        }
    }
}
