package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.EventServices
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.UserServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkViewModel : ViewModel() {

    private val userServices = UserServices()
    private val eventServices = EventServices()

    fun exists(eventId: String){
        viewModelScope.launch(Dispatchers.IO) {
            if(!eventServices.exists(eventId))
                userServices.removeFavorite(eventId)
        }
    }
}