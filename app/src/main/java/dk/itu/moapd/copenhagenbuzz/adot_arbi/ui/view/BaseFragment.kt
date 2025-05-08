package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.view

import android.content.Context
import android.content.Intent
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.viewbinding.ViewBinding
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.util.SensorProvider

/**
 * An abstract base fragment that handles shared setup logic for view binding, navigation,
 * authentication state handling, and shake detection via [SensorProvider].
 *
 * Subclasses must provide their own [ViewBinding] using [bindingInflater].
 *
 * Features:
 * - Automatically sets up top and bottom navigation bars.
 * - Displays user-specific UI (e.g., logout drawer vs. back button).
 * - Responds to shake gestures by navigating to the Add Event screen.
 *
 * @param bindingInflater A function that inflates the fragment's [ViewBinding].
 * @param timelineAction Navigation ID for the Timeline screen.
 * @param bookmarkAction Navigation ID for the Bookmarks screen.
 * @param calenderAction Navigation ID for the Calendar screen.
 * @param mapsAction Navigation ID for the Maps screen.
 * @param addEventAction Navigation ID for the Add Event screen.
 */

abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    private var timelineAction: Int,
    private var bookmarkAction: Int,
    private var calenderAction: Int,
    private var mapsAction: Int,
    private var addEventAction: Int,
) : Fragment(), SensorProvider.ShakeListener {

    private var _binding: VB? = null
    protected val binding
        get() = _binding!!

    lateinit var activity : MainActivity
    var isLoggedIn: Boolean = false

    companion object {
        const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
    }

    /**
     * Called when the fragment is attached to its context.
     * Stores a reference to the parent [MainActivity] and captures login state.
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = requireActivity() as MainActivity
        isLoggedIn = activity.isLoggedIn
    }

    /**
     * Inflates the fragment's view using the provided [bindingInflater].
     *
     * @return The root view of the binding.
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
     * Sets up UI components after the view is created.
     * - Configures top bar based on login state (drawer vs. back button).
     * - Enables "Add Event" button only for logged-in users.
     * - Initializes drawer actions (logout).
     * - Sets up bottom navigation bar.
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
        // FAB "Add Event" button visibility and action
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

        // Drawer item actions
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
     * Configures bottom navigation bar to switch between fragments.
     *
     * @param timelineAction Navigation ID for Timeline.
     * @param bookmarkAction Navigation ID for Bookmarks.
     * @param calenderAction Navigation ID for Calendar.
     * @param mapsAction Navigation ID for Maps.
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
     * Cleans up the binding reference to prevent memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }

    /**
     * Called when the fragment becomes visible. Registers for shake detection via [SensorProvider].
     */
    override fun onStart() {
        super.onStart()
        SensorProvider.init(
            context?.getSystemService(Context.SENSOR_SERVICE) as SensorManager,
            this
        )
    }

    /**
     * Called when a shake is detected. Prompts the user to navigate to the Add Event screen.
     */
    override fun onShake() {
        context?.let { context ->
            AlertDialog.Builder(context)
                .setTitle("Do you want to create an event?")
                .setItems(arrayOf("Yes, continue","No, go back")) { _, which ->
                    if(which == 0)
                        activity.navController.navigate(addEventAction)
                }.show()
        }
    }
}