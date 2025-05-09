package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prolificinteractive.materialcalendarview.CalendarDay
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.dto.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.services.EventServices
import dk.itu.moapd.copenhagenbuzz.adot_arbi.util.CustomDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
/**
 * ViewModel responsible for loading and exposing a list of events
 * to be used in calendar-based UI components.
 */
class CalenderViewModel : ViewModel() {

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> get() = _events

    private val _eventsByDate = MutableLiveData<Map<CalendarDay, List<String>>>()
    val eventsByDate: LiveData<Map<CalendarDay, List<String>>> get() = _eventsByDate

    init {
        fetchEvents()
    }

    /**
     * Fetches all events and processes them into a map grouped by date.
     */
    private fun fetchEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            val eventList = EventServices.readAllEvents()
            _events.postValue(eventList)

            val groupedEvents = eventList.groupBy(
                {
                    val dateParts = CustomDate.getDateFromEpoch(it.eventDate).split("/")
                    CalendarDay.from(dateParts[2].toInt(), dateParts[1].toInt(), dateParts[0].toInt())
                },
                { it.eventName }
            )
            _eventsByDate.postValue(groupedEvents)
        }
    }
}