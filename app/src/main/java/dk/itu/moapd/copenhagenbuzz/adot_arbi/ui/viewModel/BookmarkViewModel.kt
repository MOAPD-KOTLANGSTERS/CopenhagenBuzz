package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.services.EventServices
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.services.ImageService
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.services.UserServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for managing bookmarked events and associated data such as event validity and images.
 */
class BookmarkViewModel : ViewModel() {

    /**
     * Cleans up invalid bookmarked events that no longer exist in the events repository.
     *
     * Reads all favorited events from [UserServices] and checks their existence using [EventServices].
     * If an event does not exist anymore, it is removed from the user's favorites.
     */
    fun cleanupInvalidEvent() {
        viewModelScope.launch(Dispatchers.IO) {
            UserServices.readAllFavoriteEvents().forEach {
                withContext(Dispatchers.Main) {
                    if (!EventServices.exists(it.eventId)) {
                        UserServices.removeFavorite(it.eventId)
                    }
                }
            }
        }
    }

    /**
     * Retrieves the image URL associated with a bookmarked event and returns it via a callback.
     *
     * @param eventId The ID of the event whose image should be fetched.
     * @param onSuccess Callback that receives the image URL on success.
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
