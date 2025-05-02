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
import getAddressFromCoordinates
import getCoordinatesFromAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Random
import java.util.concurrent.TimeUnit
import java.util.Locale

class DataViewModel : ViewModel() {

    private val _events = MutableLiveData<List<DummyModel>>()
    val events: LiveData<List<DummyModel>> get() = _events

    private val _bookmarks = MutableLiveData<List<DummyModel>>()
    val bookmarks: LiveData<List<DummyModel>> get() = _bookmarks

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun fetchEvents(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val faker = Faker(Random(42))
            val data = ArrayList<DummyModel>()

            for (i in 1..50) {
                val eventName = faker.book().title()
                val type = faker.options().option("Conference", "Meetup", "Workshop", "Webinar")
                val eventDate: Date = faker.date().future(30, TimeUnit.DAYS)
                val description = faker.lorem().paragraph()
                val imageUrl = "https://picsum.photos/seed/$i/400/194"

                val faker = Faker(Locale("da"))
                val addressName = faker.address().fullAddress()

                val coordinates = getCoordinatesFromAddress(context, addressName)

                val location = coordinates?.let { (lat, lng) ->
                    val address = getAddressFromCoordinates(context, lat, lng) ?: "Unknown"
                    EventLocation(lat, lng, address)
                } ?: EventLocation(0.0, 0.0, "Unknown")

                data.add(DummyModel(eventName, type, eventDate, description, imageUrl, location))
            }

            _events.postValue(data)
            generateRandomFavorites(data)
        }
    }

    private fun generateRandomFavorites(events: List<DummyModel>) {
        val listEvents = (events.indices).take(8).sorted()
        val bookmarkedEvents = listEvents.mapNotNull { index -> events.getOrNull(index) }
        _bookmarks.postValue(bookmarkedEvents)
    }
}
