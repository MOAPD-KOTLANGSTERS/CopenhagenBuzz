package dk.itu.moapd.copenhagenbuzz.adot_arbi.viewModel

import android.os.Bundle
import androidx.fragment.app.Fragment
<<<<<<< Updated upstream
import android.view.View
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentTimeLineBinding
=======
import androidx.fragment.app.activityViewModels
import com.github.javafaker.Faker
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentTimeLineBinding
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.CustomAdapter
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.DataViewModel
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.DummyModel
import java.util.Date
import java.util.Random
import java.util.concurrent.TimeUnit
>>>>>>> Stashed changes


/**
 * A simple [Fragment] subclass.
 * Use the [TimeLineFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TimeLineFragment : BaseFragment<FragmentTimeLineBinding>(
    FragmentTimeLineBinding::inflate,
    R.id.action_timeLineFragment_self,
    R.id.action_timeLineFragment_to_bookmarksFragment,
    R.id.action_timeLineFragment_to_calenderFragment,
    R.id.action_timeLineFragment_to_mapsFragment
) {

    private val dataViewModel: DataViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

<<<<<<< Updated upstream
=======
        dataViewModel.events.observe(viewLifecycleOwner) { events ->
            val adapter = CustomAdapter(
                requireContext(),
                R.layout.item_row,
                events,
                activity?.intent?.getBooleanExtra("isLoggedIn", false) ?: false
            )
            binding.timelineListView.adapter = adapter
        }
>>>>>>> Stashed changes
    }


}