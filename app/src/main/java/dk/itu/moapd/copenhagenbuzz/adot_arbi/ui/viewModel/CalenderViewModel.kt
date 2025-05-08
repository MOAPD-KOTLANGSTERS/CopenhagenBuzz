package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.dto.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.services.EventServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for loading and exposing a list of events
 * to be used in calendar-based UI components.
 */
class CalenderViewModel : ViewModel() {

    /**
     * Backing property for the list of all events.
     */
    private val _events = MutableLiveData<List<Event>>()

    /**
     * Public [LiveData] exposing the list of events.
     */
    val events: LiveData<List<Event>> get() = _events

    /**
     * Initializes the ViewModel by triggering the first fetch of events.
     */
    init {
        fetchEvents()
    }

    /**
     * Fetches all events from the backend using [EventServices]
     * and posts the result to [_events] on the main thread.
     */
    private fun fetchEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            val eventList = EventServices.readAllEvents()
            _events.postValue(eventList)
        }
    }
}
