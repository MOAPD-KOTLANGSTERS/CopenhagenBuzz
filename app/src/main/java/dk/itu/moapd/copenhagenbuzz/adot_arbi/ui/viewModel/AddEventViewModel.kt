package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.EventServices


class AddEventViewModel : ViewModel() {

    fun updateEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            EventServices.update(event)
        }
    }

    fun addEvent(event: Event, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                EventServices.createEvent(event, context)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}