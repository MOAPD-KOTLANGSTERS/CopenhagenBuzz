package dk.itu.moapd.copenhagenbuzz.adot_arbi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.ActivityLoginBinding
import dk.itu.moapd.copenhagenbuzz.adot_arbi.view.CustomAdapter

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    companion object {
        private val TAG = CustomAdapter::class.qualifiedName
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result -> onSignInResult(result) }

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


    private fun launchSignInFlow(provider: AuthUI.IdpConfig) {
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(listOf(provider))
            .setIsSmartLockEnabled(false)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                startMainActivity()
            }
        } else {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        Intent(this, MainActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }
}