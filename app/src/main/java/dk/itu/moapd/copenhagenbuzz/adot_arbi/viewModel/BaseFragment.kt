package dk.itu.moapd.copenhagenbuzz.adot_arbi.viewModel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dk.itu.moapd.copenhagenbuzz.adot_arbi.LoginActivity
import dk.itu.moapd.copenhagenbuzz.adot_arbi.MainActivity
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R


/**
 * An abstract base fragment for initializing both the top and bottom bar.
 */
abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    private var timelineAction: Int,
    private var bookmarkAction: Int,
    private var calenderAction: Int,
    private var mapsAction: Int
) : Fragment() {

    private lateinit var activity : MainActivity
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    /**
     * Entry point for the abstract class
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = bindingInflater(inflater, container, false)
        activity = requireActivity() as MainActivity
        return _binding!!.root
    }

    companion object {
        private const val TAG = "BaseFragment"
    }

    /**
     * Method for after the view has been created will setup the top and bottom nav bar
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity.binding.imageButtonLogout.let { button ->
            with (button){
                if (requireActivity().intent.getBooleanExtra("isLoggedIn", false)) {
                    setImageResource(R.drawable.outline_account_circle_24)
                } else {
                    setImageResource(R.drawable.outline_arrow_back_24)
                }
                setOnClickListener {
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
        setupBottomNav(timelineAction, bookmarkAction, calenderAction, mapsAction)
    }

    /**
     * Function to navigate to the different fragments on the screen,
     * if more fragments are added, they should be added in the switch case.
     * @param timelineAction R.id for timeline action
     * @param bookmarkAction R.id for bookmark action
     * @param calenderAction R.id for calender action
     *  @param mapsAction R.id for maps action
     */
    private fun setupBottomNav(
        timelineAction: Int,
        bookmarkAction: Int,
        calenderAction: Int,
        mapsAction: Int
    ) {
        activity.binding.sharedBottomNavBar.setOnItemSelectedListener { menuItem ->
            val destination = when (menuItem.itemId) {
                R.id.timeline -> timelineAction
                R.id.bookmarks -> bookmarkAction
                R.id.calender -> calenderAction
                R.id.maps ->  mapsAction

                else -> return@setOnItemSelectedListener false
            }
            activity.navController.navigate(destination)
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
}