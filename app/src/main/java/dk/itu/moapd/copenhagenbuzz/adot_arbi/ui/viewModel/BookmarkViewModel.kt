package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.EventServices
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.UserServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkViewModel : ViewModel() {

    private val _bookmarks = MutableLiveData<List<Event>>()
    val bookmarks: LiveData<List<Event>> get() = _bookmarks

    fun loadFavorites() {
        viewModelScope.launch {
            val events = UserServices().readAllFavoriteEvents().mapNotNull {
                EventServices().readEventsFromId(it)
            }
            _bookmarks.postValue(events)
        }
    }

}