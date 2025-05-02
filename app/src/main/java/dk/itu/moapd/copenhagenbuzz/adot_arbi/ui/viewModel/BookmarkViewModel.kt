package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.UserServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkViewModel : ViewModel() {

    private val service = UserServices()
    private val _bookmarks = MutableLiveData<List<Event>>()
    val bookmarks: LiveData<List<Event>> get() = _bookmarks

    private fun populateBookmarks() {
        viewModelScope.launch(Dispatchers.IO) {
            val events : List<Event> = service.readAllFavoriteEvents()
            val listEvents = (events.indices).take(8).sorted()
            val bookmarkedEvents = listEvents.mapNotNull { index -> events.getOrNull(index) }
            _bookmarks.postValue(bookmarkedEvents)
        }
    }
}