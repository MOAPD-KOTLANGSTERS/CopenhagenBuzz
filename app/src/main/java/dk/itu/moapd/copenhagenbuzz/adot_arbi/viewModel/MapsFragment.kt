package dk.itu.moapd.copenhagenbuzz.adot_arbi.viewModel

import android.os.Bundle
import android.view.View
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentMapsBinding

/**
 *  A subclass of the [BaseFragment],
 *  with purposes of showing a GUI of the events on a map
 */
class MapsFragment : BaseFragment<FragmentMapsBinding>(
    FragmentMapsBinding::inflate,
    R.id.action_mapsFragment_to_timeLineFragment,
    R.id.action_mapsFragment_to_bookmarksFragment,
    R.id.action_mapsFragment_to_calenderFragment,
    R.id.action_mapsFragment_self,
    R.id.action_mapsFragment_to_addEventFragment,
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}