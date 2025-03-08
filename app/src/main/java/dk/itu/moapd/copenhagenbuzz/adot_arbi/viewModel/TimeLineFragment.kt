package dk.itu.moapd.copenhagenbuzz.adot_arbi.viewModel

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.github.javafaker.Faker
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentTimeLineBinding
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.CustomAdapter
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.DummyModel
import java.util.Date
import java.util.Random
import java.util.concurrent.TimeUnit


/**
 *  A subclass of the [BaseFragment],
 *  with purposes of managing a list of all events in ListView with dummy data
 */
class TimeLineFragment : BaseFragment<FragmentTimeLineBinding>(
    FragmentTimeLineBinding::inflate,
    R.id.action_timeLineFragment_self,
    R.id.action_timeLineFragment_to_bookmarksFragment,
    R.id.action_timeLineFragment_to_calenderFragment,
    R.id.action_timeLineFragment_to_mapsFragment,
    R.id.action_timeLineFragment_to_addEventFragment,
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val faker = Faker(Random(42))

        binding.apply {
            val data = ArrayList<DummyModel>()
            (1..50).forEach {

                val eventName = faker.book().title() // Fake event name
                val type = faker.options()
                    .option("Conference", "Meetup", "Workshop", "Webinar") // Random event type
                val eventDate: Date = faker.date().future(30, TimeUnit.DAYS) // Future date
                val description = faker.lorem().paragraph() // Random description

                data.add(
                    DummyModel(
                        eventName,
                        type,
                        eventDate,
                        description,
                        "https://picsum.photos/seed/$it/400/194"
                    )
                )
            }
            val adapter = CustomAdapter(requireContext(), R.layout.item_row, data, activity.intent.getBooleanExtra("isLoggedIn", false))
            binding.timelineListView.adapter = adapter

        }
    }
}