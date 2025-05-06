package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.EventServices
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.UserServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkViewModel : ViewModel() {
    fun exists(eventId: String){
        viewModelScope.launch(Dispatchers.IO) {
            if(!EventServices.exists(eventId))
                UserServices.removeFavorite(eventId)
        }
    }
}