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
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.EventRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.UserRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.UserServices
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel.DataViewModel

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
    private val dataViewModel: DataViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bookmarksRecyclerviewView.layoutManager = LinearLayoutManager(requireContext())

        if (isLoggedIn) {
            dataViewModel.bookmarks.observe(viewLifecycleOwner) { bookmarks ->
                val options = FirebaseRecyclerOptions.Builder<Event>()
                    .setQuery(UserRepository().db.child("favorites"), Event::class.java)
                    .setLifecycleOwner(viewLifecycleOwner)
                    .build()

                val adapter = BookmarkAdapter(
                    options
                )

                binding.bookmarksRecyclerviewView.adapter = adapter
            }
        }
        else {
            binding.bookmarksRecyclerviewView.visibility = View.GONE
        }
    }
}
