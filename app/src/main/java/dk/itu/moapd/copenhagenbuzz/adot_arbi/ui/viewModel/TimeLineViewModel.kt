package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.javafaker.Faker
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.DummyModel
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.EventLocation
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.UserServices
import getAddressFromCoordinates
import getCoordinatesFromAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Random
import java.util.concurrent.TimeUnit

class TimeLineViewModel : ViewModel() {
    val service = UserServices()

    fun addFavorite(eventId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            service.createFavorite(eventId)
        }
    }
}
