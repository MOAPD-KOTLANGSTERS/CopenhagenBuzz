package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.firebase.ui.database.FirebaseListOptions
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentTimeLineBinding
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.adapter.TimeLineAdapter
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel.TimeLineViewModel
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.dto.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.repository.EventRepository

/**
 * A fragment subclass of [BaseFragment] that displays a timeline of events in a [ListView].
 *
 * Uses Firebase's real-time database and a [TimeLineAdapter] to populate the view
 * with events ordered by their creation time. Observes the shared [TimeLineViewModel].
 */
class TimeLineFragment : BaseFragment<FragmentTimeLineBinding>(
    FragmentTimeLineBinding::inflate,
    R.id.action_timeLineFragment_self,
    R.id.action_timeLineFragment_to_bookmarksFragment,
    R.id.action_timeLineFragment_to_calenderFragment,
    R.id.action_timeLineFragment_to_mapsFragment,
    R.id.action_timeLineFragment_to_addEventFragment,
) {

    /**
     * Shared ViewModel used to manage and interact with timeline event data.
     */
    private val dataViewModel: TimeLineViewModel by activityViewModels()

    /**
     * Initializes the fragment's view and sets up the [TimeLineAdapter]
     * to display events from Firebase Realtime Database in a list.
     *
     * @param view The root view of the fragment.
     * @param savedInstanceState The previously saved instance state, or null.
     */
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
            dataViewModel,
            activity,
            options,
        )

        binding.timelineListView.adapter = adapter
    }




}
