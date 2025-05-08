package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.view
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.adapter.BookmarkAdapter
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentBookmarksBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.dto.BookmarkEvent
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.repository.UserRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel.BookmarkViewModel

/**
 * A subclass of [BaseFragment], responsible for displaying
 * and managing a list of bookmarked events for the logged-in user.
 *
 * This fragment sets up a [RecyclerView] with a [BookmarkAdapter]
 * and observes Firebase database updates to reflect changes in bookmarks.
 *
 * Navigation options to other fragments are defined in the base constructor.
 */
class BookmarksFragment : BaseFragment<FragmentBookmarksBinding>(
    FragmentBookmarksBinding::inflate,
    R.id.action_bookmarksFragment_to_timeLineFragment,
    R.id.action_bookmarksFragment_self,
    R.id.action_bookmarksFragment_to_calenderFragment,
    R.id.action_bookmarksFragment_to_mapsFragment,
    R.id.action_bookmarksFragment_to_addEventFragment,
) {

    /**
     * The adapter used to display a list of bookmarked events.
     */
    private var adapter: BookmarkAdapter? = null

    /**
     * ViewModel for managing bookmark-related data and operations.
     */
    private val bookmarkViewModel: BookmarkViewModel by viewModels()

    /**
     * Called when the fragment becomes visible to the user.
     * Triggers cleanup of any invalid or deleted bookmarked events.
     */
    override fun onResume() {
        super.onResume()
        bookmarkViewModel.cleanupInvalidEvent()
    }

    /**
     * Called after the fragment’s view has been created.
     * Sets up the [RecyclerView] and populates it with the user's bookmarks
     * if the user is authenticated. Hides the list otherwise.
     *
     * @param view The created view hierarchy associated with the fragment.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bookmarksRecyclerviewView.layoutManager = LinearLayoutManager(requireContext())

        if (isLoggedIn) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

            val query = UserRepository.db
                .child(userId)
                .child("favorites")
                .orderByChild("eventDate")

            val options = FirebaseRecyclerOptions.Builder<BookmarkEvent>()
                .setQuery(query, BookmarkEvent::class.java)
                .setLifecycleOwner(viewLifecycleOwner)
                .build()

            adapter = BookmarkAdapter(options, bookmarkViewModel)
            binding.bookmarksRecyclerviewView.adapter = adapter
            binding.textViewGuestMessage.visibility = View.GONE
        } else {
            binding.bookmarksRecyclerviewView.visibility = View.GONE
            binding.textViewGuestMessage.visibility = View.VISIBLE
        }

    }


}
