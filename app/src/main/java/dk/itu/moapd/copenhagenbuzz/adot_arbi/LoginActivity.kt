package dk.itu.moapd.copenhagenbuzz.adot_arbi

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { result ->
        onSignInResult(result)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        
        // Google Login Button
        findViewById<View>(R.id.button_google_login).setOnClickListener {
            launchSignInFlow(AuthUI.IdpConfig.GoogleBuilder().build())
        }

        // Email Login Button
        findViewById<View>(R.id.button_email_login).setOnClickListener {
            launchSignInFlow(AuthUI.IdpConfig.EmailBuilder().build())
        }

        // Guest Login Button
        findViewById<View>(R.id.button_guest).setOnClickListener {
            startMainActivity()
        }

    }


    private fun launchSignInFlow(provider: AuthUI.IdpConfig) {
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(listOf(provider))
            .setIsSmartLockEnabled(true)
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