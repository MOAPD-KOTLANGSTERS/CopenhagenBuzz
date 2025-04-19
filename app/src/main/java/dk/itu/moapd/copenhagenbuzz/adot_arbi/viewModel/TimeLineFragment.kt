package dk.itu.moapd.copenhagenbuzz.adot_arbi.viewModel

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentTimeLineBinding
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.CustomAdapter
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.DataViewModel



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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataViewModel.events.observe(viewLifecycleOwner) { events ->
            val adapter = CustomAdapter(
                requireContext(),
                R.layout.item_row,
                events,
                activity?.intent?.getBooleanExtra("isLoggedIn", false) ?: false
            )
            binding.timelineListView.adapter = adapter
        }
    }
}