package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.view

import BookmarkAdapter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentBookmarksBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.BookmarkEvent
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.UserRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel.BookmarkViewModel

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
    private var adapter: BookmarkAdapter? = null

    private val bookmarkViewModel: BookmarkViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(BookmarkViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return BookmarkViewModel(requireContext()) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bookmarksRecyclerviewView.layoutManager = LinearLayoutManager(requireContext())

        if (isLoggedIn) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

            val query = UserRepository().db
                .child(userId)
                .child("favorites")
                .orderByChild("eventDate")

            val options = FirebaseRecyclerOptions.Builder<BookmarkEvent>()
                .setQuery(query, BookmarkEvent::class.java)
                .setLifecycleOwner(viewLifecycleOwner)
                .build()

            adapter = BookmarkAdapter(options, bookmarkViewModel)
            binding.bookmarksRecyclerviewView.adapter = adapter
        } else {
            binding.bookmarksRecyclerviewView.visibility = View.GONE
        }
    }
}

