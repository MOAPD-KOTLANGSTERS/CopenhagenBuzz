package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.EventServices
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.UserServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookmarkViewModel(context: Context) : ViewModel() {

    private val userServices = UserServices()
    private val eventServices = EventServices(context)

    private val _bookmarks = MutableLiveData<List<Event>>()
    val bookmarks: LiveData<List<Event>> get() = _bookmarks

    fun exists(eventId: String){
        viewModelScope.launch(Dispatchers.IO) {
            if(!eventServices.exists(eventId))
                userServices.removeFavorite(eventId)
        }
    }
}