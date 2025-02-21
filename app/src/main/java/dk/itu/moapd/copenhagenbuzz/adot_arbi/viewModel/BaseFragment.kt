package dk.itu.moapd.copenhagenbuzz.adot_arbi.viewModel

import android.content.Intent
import android.os.Bundle
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
    private var calenderAction: Int
) : Fragment() {

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
        return _binding!!.root
    }

    /**
     * Method for after the view has been created will setup the top and bottom nav bar
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.root?.findViewById<ImageButton>(R.id.image_button_logout).let { button ->
            with (button!!){
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
        setupBottomNav(timelineAction, bookmarkAction, calenderAction)
    }

    /**
     * Function to navigate to the different fragments on the screen,
     * if more fragments are added, they should be added in the switch case.
     * @param timelineAction R.id for timeline action
     * @param bookmarkAction R.id for bookmark action
     * @param calenderAction R.id for calender action
     */
    private fun setupBottomNav(
        timelineAction: Int,
        bookmarkAction: Int,
        calenderAction: Int
    ) {
        _binding?.root?.findViewById<BottomNavigationView>(R.id.shared_bottom_nav_bar)?.setOnItemSelectedListener { menuItem ->
            val destination = when (menuItem.itemId) {
                R.id.timeline -> timelineAction
                R.id.bookmarks -> bookmarkAction
                R.id.calender -> calenderAction
                else -> return@setOnItemSelectedListener false
            }
            val activity = requireActivity() as MainActivity
            activity.navController.navigate(destination)
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
}