package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.javafaker.Faker
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.DummyModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Random
import java.util.concurrent.TimeUnit

class DataViewModel : ViewModel() {

    private val _events = MutableLiveData<List<DummyModel>>()
    val events: LiveData<List<DummyModel>> get() = _events

    private val _bookmarks = MutableLiveData<List<DummyModel>>()
    val bookmarks: LiveData<List<DummyModel>> get() = _bookmarks

    init {
        fetchEvents()
    }

    private fun fetchEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            val faker = Faker(Random(42))
            val data = ArrayList<DummyModel>()

            (1..50).forEach {
                val eventName = faker.book().title()
                val type = faker.options().option("Conference", "Meetup", "Workshop", "Webinar")
                val eventDate: Date = faker.date().future(30, TimeUnit.DAYS)
                val description = faker.lorem().paragraph()
                val imageUrl = "https://picsum.photos/seed/$it/400/194"

                data.add(DummyModel(eventName, type, eventDate, description, imageUrl))
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