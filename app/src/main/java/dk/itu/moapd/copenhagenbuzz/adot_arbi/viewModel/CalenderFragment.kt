package dk.itu.moapd.copenhagenbuzz.adot_arbi.viewModel

import android.os.Bundle
import android.view.View
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentCalenderBinding

/**
 *  A subclass of the [BaseFragment],
 *  with purposes of managing the list of interested bookmarks, displayed as a calender.
 */
class CalenderFragment : BaseFragment<FragmentCalenderBinding>(
    FragmentCalenderBinding::inflate,
    R.id.action_calenderFragment_to_timeLineFragment,
    R.id.action_calenderFragment_to_bookmarksFragment,
    R.id.action_calenderFragment_self,
    R.id.action_calenderFragment_to_mapsFragment
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO : implement bookmarks
    }

}