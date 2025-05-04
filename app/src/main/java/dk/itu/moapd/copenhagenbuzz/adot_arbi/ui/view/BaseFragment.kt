package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.firebase.auth.FirebaseAuth
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


    private var _binding: VB? = null
    protected val binding
        get() = _binding!!

    lateinit var activity : MainActivity
    var isLoggedIn: Boolean = false

    companion object {
        const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = requireActivity() as MainActivity
        isLoggedIn = activity.isLoggedIn
    }

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
        return _binding!!.root
    }

    /**
     * Method for after the view has been created will setup the top and bottom nav bar
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Dynamically change the icon in the top-bar based on the user's authentication state
        activity.binding.imageButtonLogout.let { button ->

            // Set the icon based on the user's state
            button.setImageResource(
                if (isLoggedIn) R.drawable.outline_account_circle_24 else R.drawable.outline_arrow_back_24
            )

            // Set the click behavior
            button.setOnClickListener {
                if (isLoggedIn) {
                    activity.binding.drawerLayout.openDrawer(activity.binding.navigationView)
                } else {
                    // Navigate back to the LoginActivity for guest users
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                }
            }
        }

       if(isLoggedIn) {
            activity.binding.imageButtonAddEvent.let { button ->
                button.setImageResource(R.drawable.outline_add_24)
                button.setOnClickListener {
                    activity.navController.navigate(addEventAction)
                }
            }
        } else {
            activity.binding.imageButtonAddEvent.visibility = View.GONE
        }


        activity.binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_profile -> {
                    // Navigate to the profile screen or handle profile action

                }
                R.id.menu_logout -> {
                    // Log out the user
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                }
            }
            activity.binding.drawerLayout.closeDrawer(activity.binding.navigationView)
            true
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