package dk.itu.moapd.copenhagenbuzz.adot_arbi

import android.content.Intent
import android.os.Bundle
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

        loginBinding.buttonLogin.setOnClickListener{
            startExplicitIntent(
                MainActivity::class.java,
                listOf { it.putExtra("isLoggedIn", true) }
            )}
        loginBinding.buttonGuest.setOnClickListener{
            startExplicitIntent(
                MainActivity::class.java,
                listOf { it.putExtra("isLoggedIn", false) }
            )}
    }

    /**
     * A method for giving an explicit intent when starting a new activity
     */
    private fun startExplicitIntent(
        c : Class<*>,
        options : List<(Intent) -> Unit>
    ) {
        loginBinding.buttonGuest.setOnClickListener {
            startActivity(Intent(this, c).apply {
                options.forEach { it(this) }
            })
            finish()
        }
    }
}