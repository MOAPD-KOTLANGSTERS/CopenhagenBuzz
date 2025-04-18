package dk.itu.moapd.copenhagenbuzz.adot_arbi.viewModel

import android.os.Bundle
import android.view.View
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentMapsBinding

class MapsFragment : BaseFragment<FragmentMapsBinding>(
    FragmentMapsBinding::inflate,
    R.id.action_mapsFragment_to_timeLineFragment,
    R.id.action_mapsFragment_to_bookmarksFragment,
    R.id.action_mapsFragment_to_calenderFragment,
    R.id.action_mapsFragment_self
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
<<<<<<< Updated upstream
<<<<<<< Updated upstream
=======

>>>>>>> Stashed changes
=======

>>>>>>> Stashed changes

    }

}