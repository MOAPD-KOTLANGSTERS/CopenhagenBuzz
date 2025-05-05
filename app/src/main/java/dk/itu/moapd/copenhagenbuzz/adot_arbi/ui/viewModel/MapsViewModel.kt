package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.EventServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapsViewModel() : ViewModel() {
    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> get() = _events
    private val eventServices = EventServices()

    fun getAllEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            _events.postValue(eventServices.readAllEvents())
        }
    }
}