package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.BookmarkEvent
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.EventRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.ImageService
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.UserServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for the timeline screen, handling the selection of events,
 * bookmark actions, and image loading related to events.
 */
class TimeLineViewModel : ViewModel() {

    /**
     * LiveData that holds the currently selected event.
     */
    private val _selectedEvent = MutableLiveData<Event?>()
    val selectedEvent: MutableLiveData<Event?>
        get() = _selectedEvent

    /**
     * Initializes the selected event to null.
     */
    init {
        selectedEvent.value = null
    }

    /**
     * Sets the currently selected event.
     *
     * @param event The event to select, or null to clear the selection.
     */
    fun setEvent(event: Event? = null) {
        _selectedEvent.value = event
    }

    /**
     * Checks whether a given event is favorited by the user.
     *
     * @param eventId The ID of the event to check.
     * @param onResult Callback to receive the boolean result on the main thread.
     */
    fun isFavorite(eventId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = UserServices.isFavorite(eventId)
            withContext(Dispatchers.Main) {
                onResult(result)
            }
        }
    }

    /**
     * Adds an event to the user's favorites list.
     *
     * @param eventId The ID of the event to favorite.
     * @param onSuccess Callback returning whether the event is now marked as favorite.
     */
    fun addFavorite(eventId: String, onSuccess: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val event = EventRepository.readEventsFromId(eventId)
            if (event != null) {
                UserServices.favorite(
                    BookmarkEvent(
                        eventId = eventId,
                        eventType = event.eventType,
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

    /**
     * Loads the image URL for a given event from the image service.
     *
     * @param eventId The ID of the event whose image is to be loaded.
     * @param onSuccess Callback to receive the image URL on success.
     */
    fun readImage(eventId: String, onSuccess: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            ImageService.read(eventId)
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        onSuccess(it)
                    }
                }
        }
    }
}
