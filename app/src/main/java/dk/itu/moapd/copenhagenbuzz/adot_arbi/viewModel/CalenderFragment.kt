package dk.itu.moapd.copenhagenbuzz.adot_arbi.viewModel

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentCalenderBinding

class CalenderFragment : BaseFragment<FragmentCalenderBinding>(FragmentCalenderBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomNav(
            binding.bottomNavBar.bottomNavBar,
            findNavController(),
            R.id.action_calenderFragment_to_timeLineFragment,
            R.id.action_calenderFragment_to_bookmarksFragment,
            R.id.action_calenderFragment_self
        )
    }

}