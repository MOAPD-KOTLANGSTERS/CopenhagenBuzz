package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.UserServices
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

/**
 * Activity responsible for handling user authentication using FirebaseUI.
 *
 * Offers login options via Google, email/password, or as a guest (anonymous access).
 * Redirects authenticated users to [MainActivity] and registers new users via [UserServices].
 */
class LoginActivity : AppCompatActivity() {

    /**
     * View binding for the login activity layout.
     */
    private lateinit var binding: ActivityLoginBinding

    companion object {
        /**
         * Tag used for logging within the activity.
         */
        private val TAG = LoginActivity::class.qualifiedName
    }

    /**
     * Launcher for the FirebaseUI authentication flow.
     */
    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result -> onSignInResult(result) }

    /**
     * Initializes the login screen and sets up login button actions.
     *
     * @param savedInstanceState Previously saved state for re-creation, or null if none.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Google Login Button
        binding.buttonGoogleLogin.setOnClickListener {
            launchSignInFlow(AuthUI.IdpConfig.GoogleBuilder().build())
        }

        // Email Login Button
        binding.buttonEmailLogin.setOnClickListener {
            launchSignInFlow(AuthUI.IdpConfig.EmailBuilder().build())
        }

        // Guest Login Button
        binding.buttonGuest.setOnClickListener {
            startMainActivity()
        }
    }

    /**
     * Launches the FirebaseUI sign-in flow for the selected provider.
     *
     * @param provider The selected [AuthUI.IdpConfig] for sign-in.
     */
    private fun launchSignInFlow(provider: AuthUI.IdpConfig) {
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(listOf(provider))
            .setIsSmartLockEnabled(true)
            .build()
        signInLauncher.launch(signInIntent)
    }

    /**
     * Handles the result of the FirebaseUI sign-in process.
     *
     * If authentication is successful, the user is created in Firestore (if new)
     * and redirected to the main screen. On failure, fallback to guest access.
     *
     * @param result The authentication result from FirebaseUI.
     */
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            Log.d(TAG, "onSignInResult: $user")
            if (user != null) {
                lifecycleScope.launch {
                    UserServices.createUser(user)
                    startMainActivity()
                }
            }
        } else {
            FirebaseAuth.getInstance().signOut()
            startMainActivity()
        }
    }

    /**
     * Starts the main application screen and closes the login screen.
     */
    private fun startMainActivity() {
        Intent(this, MainActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }
}
