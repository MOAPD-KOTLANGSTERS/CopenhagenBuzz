package dk.itu.moapd.copenhagenbuzz.adot_arbi.viewModel

import com.github.javafaker.Faker
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentTimeLineBinding
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.CustomAdapter
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.DummyModel
import java.util.Random


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val faker = Faker(Random(42))

        binding.apply {
            val data = ArrayList<DummyModel>()
            (1..50).forEach {
                val address = faker.address()

                data.add(
                    DummyModel(
                        address.cityName(),
                        address.zipCode(),
                        address.country(),
                        faker.lorem().paragraph(),
                        "https://picsum.photos/seed/$it/400/194"
                    )
                )
            }
            val adapter = CustomAdapter(requireContext(), R.layout.item_row, data)
            binding.timelineListView.adapter = adapter

        }
    }
}