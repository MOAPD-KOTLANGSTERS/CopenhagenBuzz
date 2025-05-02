package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.view

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.firebase.ui.database.FirebaseListOptions
import com.google.firebase.database.FirebaseDatabase
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentTimeLineBinding
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.adapter.EventAdapter
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel.DataViewModel
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.EventRepository


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

    private val dataViewModel: DataViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataViewModel.fetchEvents(requireContext())


        val options = FirebaseListOptions.Builder<Event>()
            .setQuery(EventRepository().db, Event::class.java)
            .setLayout(R.layout.item_row)
            .setLifecycleOwner(viewLifecycleOwner)
            .build()


        dataViewModel.events.observe(viewLifecycleOwner) { events ->
            val adapter = EventAdapter(
                requireContext(),
                R.layout.item_row,
                isLoggedIn,
                options,
            )
            binding.timelineListView.adapter = adapter
        }
    }
}