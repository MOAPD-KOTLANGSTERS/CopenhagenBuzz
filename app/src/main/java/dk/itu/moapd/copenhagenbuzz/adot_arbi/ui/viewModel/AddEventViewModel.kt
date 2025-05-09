package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.dto.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.services.EventServices
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.services.ImageService
import kotlinx.coroutines.withContext

/**
 * ViewModel responsible for handling event creation, updates, deletions,
 * and image uploads/downloads for the "Add Event" feature.
 */
class AddEventViewModel : ViewModel() {

    /**
     * Updates an existing event in the backend via [EventServices].
     *
     * @param event The event to update.
     */
    fun updateEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            EventServices.update(event)
        }
    }

    /**
     * Adds a new event using [EventServices] and optionally uploads an image using [ImageService].
     *
     * @param event The event data to create.
     * @param uri The URI of the image file to upload (optional).
     * @param context The application context, required for event creation.
     */
    fun addEvent(event: Event, uri: Uri?, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            EventServices.createEvent(event, context)
                .onSuccess { result ->
                    uri?.let {
                        ImageService.create(result.id!!, it)
                        Log.d("Test", uri.toString())
                    }
                }
        }
    }

    /**
     * Reads an image associated with an event using [ImageService] and delivers the result on the main thread.
     *
     * @param eventId The ID of the event whose image should be read.
     * @param onSuccess Callback function to handle the image URL on success.
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

    /**
     * Deletes the event and its associated image using [EventServices] and [ImageService].
     *
     * @param event The event to delete.
     * @param onSuccess Callback invoked upon successful deletion.
     */
    fun deleteEvent(event: Event, onSuccess: () -> Unit) {
        viewModelScope.launch {
            EventServices.deleteEvent(event)
                .onSuccess {
                    ImageService.delete(event.id!!)
                    withContext(Dispatchers.Main) {
                        onSuccess()
                    }
                }
        }
    }
}
