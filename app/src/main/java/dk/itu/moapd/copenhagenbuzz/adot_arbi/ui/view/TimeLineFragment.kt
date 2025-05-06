package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.view

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.firebase.ui.database.FirebaseListOptions
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentTimeLineBinding
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.adapter.TimeLineAdapter
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel.TimeLineViewModel
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

    private val dataViewModel: TimeLineViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val query = EventRepository.db
            .orderByChild("createdAt")

        val options = FirebaseListOptions.Builder<Event>()
            .setQuery(query, Event::class.java)
            .setLayout(R.layout.item_row)
            .setLifecycleOwner(viewLifecycleOwner)
            .build()

        val adapter = TimeLineAdapter(
            requireContext(),
            R.layout.item_row,
            isLoggedIn,
            dataViewModel,
            activity,
            options,
        )
        binding.timelineListView.adapter = adapter
    }
}