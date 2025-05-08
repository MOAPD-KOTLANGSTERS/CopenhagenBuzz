package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.EventServices
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.ImageService
import kotlinx.coroutines.withContext


class AddEventViewModel : ViewModel() {

    fun updateEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            EventServices.update(event)
        }
    }

    fun addEvent(event: Event, uri: Uri?, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            EventServices.createEvent(event, context)
                .onSuccess { result ->
                    uri?.let { ImageService.create(result.id!!, it); Log.d("Test", uri.toString()) }
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

    fun deleteEvent(event: Event, onSuccess: () -> Unit) {
        viewModelScope.launch {
            EventServices.deleteEvent(event)
                .onSuccess {
                    ImageService.delete(event.id!!)
                    withContext(Dispatchers.Main){
                        onSuccess()
                    }
                }
        }
    }
}