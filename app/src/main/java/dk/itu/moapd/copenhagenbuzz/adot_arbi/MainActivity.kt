package dk.itu.moapd.copenhagenbuzz.adot_arbi

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.ActivityMainBinding


/**
 * MainActivity serves as the entry point of the application.
 * It initializes the UI components, binds views using View Binding,
 * and handles user interactions such as date selection and event creation.
 */
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var navController : NavController

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
    }




}