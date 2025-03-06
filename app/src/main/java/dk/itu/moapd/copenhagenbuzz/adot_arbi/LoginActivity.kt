package dk.itu.moapd.copenhagenbuzz.adot_arbi

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.ActivityLoginBinding
import dk.itu.moapd.copenhagenbuzz.adot_arbi.viewModel.BaseFragment
import java.io.Serializable

/**
 *  An Activity for authenticating a user, before gaining access to the main activity.
 */
class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        startExplicitIntent(
            loginBinding.buttonLogin,
            MainActivity::class.java,
            listOf { it.putExtra("isLoggedIn", true)})

        startExplicitIntent(
            loginBinding.buttonGuest,
            MainActivity::class.java,
            listOf { it.putExtra("isLoggedIn", false)})
    }

    /**
     * A method for adding an event listener to start a new activity with explicit intent
     * @param v view element to attach event listener on
     * @param c the activity to navigate to
     * @param options a list of higher-order function to apply with the intent
     */
    private fun startExplicitIntent(v : View ,c : Class<*>, options : List<(Intent) -> Unit>?) {
        options?.let { lst ->
            v.setOnClickListener {
                startActivity(Intent(this, c).apply {
                    lst.forEach { extra -> extra(this) }
                })
                finish()
            }
        }
    }
}