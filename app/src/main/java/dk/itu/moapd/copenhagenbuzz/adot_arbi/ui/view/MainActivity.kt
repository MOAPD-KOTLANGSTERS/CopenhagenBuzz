package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.ActivityMainBinding
import dk.itu.moapd.copenhagenbuzz.adot_arbi.util.SensorProvider

/**
 * MainActivity serves as the entry point of the application.
 *
 * It sets up the navigation graph, handles authentication-related UI logic,
 * and connects UI elements like the logout and add event buttons.
 * Unauthenticated users are redirected to the [LoginActivity].
 */
class MainActivity : AppCompatActivity() {

    companion object {
        /**
         * Tag used for logging purposes.
         */
        private val TAG = MainActivity::class.qualifiedName
    }

    /**
     * View binding for accessing layout views.
     */
    lateinit var binding: ActivityMainBinding

    /**
     * Navigation controller used to manage app fragment navigation.
     */
    lateinit var navController: NavController

    /**
     * Property that checks whether the current user is authenticated and not anonymous.
     */
    val isLoggedIn: Boolean
        get() {
            val user = FirebaseAuth.getInstance().currentUser
            return user != null && !user.isAnonymous
        }

    /**
     * Initializes the activity, sets up UI layout, navigation, and user interface logic.
     *
     * @param savedInstanceState The previously saved instance state, if any.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        // Inflate layout with view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up NavHostFragment and navigation controller
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController

        // Configure logout/account button behavior
        binding.imageButtonLogout.let { button ->
            button.setImageResource(
                if (isLoggedIn) R.drawable.outline_account_circle_24
                else R.drawable.outline_arrow_back_24
            )
            button.setOnClickListener {
                if (isLoggedIn) {
                    FirebaseAuth.getInstance().signOut()
                    recreate() // Restart activity to refresh UI
                } else {
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }

        // Show/hide add event button based on authentication
        binding.imageButtonAddEvent.visibility =
            if (isLoggedIn) View.VISIBLE else View.GONE
        binding.imageButtonAddEvent.setOnClickListener {
            navController.navigate(R.id.action_to_addEventFragment)
        }
    }

    /**
     * Checks for user authentication status on start.
     * Redirects to login screen if no user is logged in or if the user is anonymous.
     */
    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        Log.d(TAG, "onStart: $user")
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save UI state, e.g., navigation state or user-specific data
        outState.putBoolean("isLoggedIn", isLoggedIn)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Restore UI state
        val restoredIsLoggedIn = savedInstanceState.getBoolean("isLoggedIn", false)
        if (restoredIsLoggedIn != isLoggedIn) {
            recreate() // Refresh UI if login state changes
        }
    }

    /**
     * Stops sensor operations and finishes the activity on destruction.
     */
    override fun onDestroy() {
        super.onDestroy()
        SensorProvider.stop()
        finish()
    }
}
