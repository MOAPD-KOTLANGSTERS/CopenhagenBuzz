package dk.itu.moapd.copenhagenbuzz.adot_arbi

import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.ActivityMainBinding


/**
 * MainActivity serves as the entry point of the application.
 * It initializes the UI components, binds views using View Binding,
 * and handles user interactions such as date selection and event creation.
 */
class MainActivity : AppCompatActivity() {

    /**
     *  The [ActivityMainBinding] for the parent XML file
     */

    lateinit var binding: ActivityMainBinding

    /**
     * The original instance of [NavController]
     */
    lateinit var navController: NavController

    val isLoggedIn: Boolean
        get() = FirebaseAuth.getInstance().currentUser != null

    /**
     * Called when the activity is first created.
     * Initializes the UI, sets up window insets, and attaches event listeners.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        // Inflate the layout using View Binding and set the content view.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up NavHostFragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController

        // Update the logout button dynamically based on authentication status
        binding.imageButtonLogout.let { button ->

            button.setImageResource(
                if (isLoggedIn) R.drawable.outline_account_circle_24 else R.drawable.outline_arrow_back_24
            )
            button.setOnClickListener {
                if (isLoggedIn) {
                    FirebaseAuth.getInstance().signOut()
                    recreate() // Refresh the activity to update UI
                } else {
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }


        // Update the add event button visibility based on authentication status
        binding.imageButtonAddEvent.visibility =
            if (isLoggedIn) View.VISIBLE else View.GONE
        binding.imageButtonAddEvent.setOnClickListener {
            navController.navigate(R.id.action_to_addEventFragment)
        }



    }
}