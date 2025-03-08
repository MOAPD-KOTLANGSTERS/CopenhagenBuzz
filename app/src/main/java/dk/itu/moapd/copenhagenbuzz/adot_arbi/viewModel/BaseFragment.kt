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
 * An abstract [BaseFragment] for initializing boilerplate code,
 * and with option for building both the top and bottom bar
 * @param bindingInflater a function reference for the fragments layout inflater.
 * @param VB the viewbinding of the subclass.
 */
abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    private var timelineAction: Int,
    private var bookmarkAction: Int,
    private var calenderAction: Int,
    private var mapsAction: Int,
    private var addEventAction: Int,
) : Fragment() {

    protected lateinit var activity : MainActivity
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    /**
     * Entry-point for the abstract class for initializing the viewbinding.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /*  Since the parent class viewbinding doesn't have an inflate method,
            we use a function reference instead, from the parameter.  */
        _binding = bindingInflater(inflater, container, false)
        activity = requireActivity() as MainActivity
        return _binding!!.root
    }

    /**
     * Method for after the view has been created will setup the top and bottom nav bar
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // dynamically change the icon in the top-bar based on the intent
        activity.binding.imageButtonLogout.let { button ->
            with (button){
                if (activity.intent.getBooleanExtra("isLoggedIn", false)) {
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

        // TODO : Do we keep this? it doesn't work
        if (activity.intent.getBooleanExtra("isLoggedIn", false))
            with (activity.binding.imageButtonAddEvent) {
                visibility = View.VISIBLE
                setOnClickListener { activity.navController.navigate(addEventAction) }
            }


        setupBottomNav(timelineAction, bookmarkAction, calenderAction, mapsAction)
    }

    /**
     * Function to navigate to the different fragments on the screen,
     * if more fragments are added, they should be added in the switch case.
     * @param timelineAction R.id for timeline action
     * @param bookmarkAction R.id for bookmark action
     * @param calenderAction R.id for calender action
     * @param mapsAction R.id for maps action
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


    /**
     * A method for destroying the fragment, ending its lifecycle and prevent memory issues
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
}