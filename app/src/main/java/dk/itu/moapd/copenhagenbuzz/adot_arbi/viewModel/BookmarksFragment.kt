package dk.itu.moapd.copenhagenbuzz.adot_arbi.viewModel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentBookmarksBinding

/**
 * A simple [Fragment] subclass.
 * Use the [BookmarksFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookmarksFragment : BaseFragment<FragmentBookmarksBinding>(
    FragmentBookmarksBinding::inflate,
    R.id.action_bookmarksFragment_to_timeLineFragment2,
    R.id.action_bookmarksFragment_self,
    R.id.action_bookmarksFragment_to_calenderFragment2,
    R.id.action_bookmarksFragment_to_mapsFragment
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
