package dk.itu.moapd.copenhagenbuzz.adot_arbi.viewModel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentTimeLineBinding


/**
 * A simple [Fragment] subclass.
 * Use the [TimeLineFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimeLineFragment : BaseFragment<FragmentTimeLineBinding>(FragmentTimeLineBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupBottomNav(
            binding.bottomNavBar.bottomNavBar,
            findNavController(),
            R.id.action_timeLineFragment_self,
            R.id.action_timeLineFragment_to_bookmarksFragment,
            R.id.action_timeLineFragment_to_calenderFragment
        )
    }


}