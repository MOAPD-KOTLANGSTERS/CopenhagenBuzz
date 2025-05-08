package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.EventServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel used for the Maps feature to retrieve and expose a list of all events.
 */
class MapsViewModel : ViewModel() {

    /**
     * Internal mutable list of events.
     */
    private val _events = MutableLiveData<List<Event>>()

    /**
     * Public immutable [LiveData] exposing the list of events to observers.
     */
    val events: LiveData<List<Event>> get() = _events

    /**
     * Loads all events asynchronously from [EventServices] and posts the results
     * to the [_events] LiveData so they can be observed on the UI thread.
     */
    fun getAllEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            _events.postValue(EventServices.readAllEvents())
        }
    }
}
