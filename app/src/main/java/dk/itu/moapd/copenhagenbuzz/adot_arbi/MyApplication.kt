package dk.itu.moapd.copenhagenbuzz.adot_arbi

import android.app.Application
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dk.itu.moapd.copenhagenbuzz.adot_arbi.view.CustomAdapter
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv

class MyApplication : Application() {

    companion object {
        private val TAG = CustomAdapter::class.qualifiedName
    }

    override fun onCreate() {
        super.onCreate()
        val dotenv : Dotenv = dotenv {
            directory = "/assets"
            filename = "env"
        }
        val DATABASE_URL: String = dotenv["DATABASE_URL"]
        Firebase.database(DATABASE_URL).setPersistenceEnabled(true)

    }
}