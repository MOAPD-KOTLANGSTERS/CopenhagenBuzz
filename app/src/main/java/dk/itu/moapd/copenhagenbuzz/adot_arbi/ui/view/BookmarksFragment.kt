package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.view

import BookmarkAdapter
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import android.view.View
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentBookmarksBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.UserRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.UserServices
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel.BookmarkViewModel
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel.TimeLineViewModel

/**
 *  A subclass of the [BaseFragment],
 *  with purposes of managing a list of favorite events of the user.
 */
class BookmarksFragment : BaseFragment<FragmentBookmarksBinding>(
    FragmentBookmarksBinding::inflate,
    R.id.action_bookmarksFragment_to_timeLineFragment,
    R.id.action_bookmarksFragment_self,
    R.id.action_bookmarksFragment_to_calenderFragment,
    R.id.action_bookmarksFragment_to_mapsFragment,
    R.id.action_bookmarksFragment_to_addEventFragment,

    ) {
    private val dataViewModel: BookmarkViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bookmarksRecyclerviewView.layoutManager = LinearLayoutManager(requireContext())

        if (isLoggedIn) {
            dataViewModel.loadFavorites()
            dataViewModel.bookmarks.observe(viewLifecycleOwner) { events ->
                val adapter = BookmarkAdapter(events)
                binding.bookmarksRecyclerviewView.adapter = adapter
            }
        }
        else {
            binding.bookmarksRecyclerviewView.visibility = View.GONE
        }
    }
}
