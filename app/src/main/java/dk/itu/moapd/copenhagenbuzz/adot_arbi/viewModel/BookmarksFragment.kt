package dk.itu.moapd.copenhagenbuzz.adot_arbi.viewModel

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import android.view.View
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentBookmarksBinding
import androidx.recyclerview.widget.LinearLayoutManager
import dk.itu.moapd.copenhagenbuzz.adot_arbi.adapter.BookmarkAdapter
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.DataViewModel

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

<<<<<<< Updated upstream
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

=======
    ) {
    private val dataViewModel: DataViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bookmarksRecyclerviewView.layoutManager = LinearLayoutManager(requireContext())


        val isLoggedin = activity?.intent?.getBooleanExtra("isLoggedIn", false) ?: false

        if (isLoggedin) {
            dataViewModel.bookmarks.observe(viewLifecycleOwner) { bookmarks ->
                val adapter = BookmarkAdapter(bookmarks)
                binding.bookmarksRecyclerviewView.adapter = adapter
            }
        }
        else {
            binding.bookmarksRecyclerviewView.visibility = View.GONE
        }
>>>>>>> Stashed changes
    }

}
