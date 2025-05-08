package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.dto.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.services.EventServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel used for the Maps feature to manage and expose event data
 * and handle map-related logic such as adding markers.
 */
class MapsViewModel : ViewModel() {

    /**
     * Internal mutable list of events.
     */
    private val _events = MutableLiveData<List<Event>>()

    /**
     * Public LiveData exposing the list of events.
     */
    val events: LiveData<List<Event>> get() = _events

    /**
     * Fetches all events from the backend using [EventServices]
     * and posts the result to [_events].
     */
    fun getAllEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            val eventList = EventServices.readAllEvents()
            _events.postValue(eventList)
        }
    }

    /**
     * Adds markers for the provided events to the given [GoogleMap].
     *
     * @param map The [GoogleMap] instance to add markers to.
     */
    fun setupMap(map: GoogleMap) {
        _events.value?.forEach { event ->
            val location = LatLng(event.eventLocation.lat, event.eventLocation.long)
            val marker = map.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(event.eventName)
            )
            marker?.tag = event
        }
    }
}