package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.EventServices
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.ImageService
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.UserServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookmarkViewModel : ViewModel() {
    fun cleanupInvalidEvent(){
        viewModelScope.launch(Dispatchers.IO) {
            UserServices.readAllFavoriteEvents().forEach {
                withContext(Dispatchers.Main) {
                    if(!EventServices.exists(it.eventId))
                        UserServices.removeFavorite(it.eventId)
                }
            }
        }
    }

    fun readImage(eventId: String, onSuccess: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            ImageService.read(eventId)
                .onSuccess {
                    withContext(Dispatchers.Main){
                        onSuccess(it)
                    }
                }
        }
    }
}