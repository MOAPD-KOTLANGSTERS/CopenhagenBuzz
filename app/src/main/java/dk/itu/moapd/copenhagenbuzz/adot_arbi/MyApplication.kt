package dk.itu.moapd.copenhagenbuzz.adot_arbi

import android.app.Application
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.adapter.CustomAdapter
import dk.itu.moapd.copenhagenbuzz.adot_arbi.util.DotEnvManager
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv

class MyApplication : Application() {

    companion object {
        private val TAG = CustomAdapter::class.qualifiedName
    }


    private val DATABASE_URL : String = DotEnvManager.dotenv!!["DATABASE_URL"]

    override fun onCreate() {
        super.onCreate()

        Firebase.database(DATABASE_URL).setPersistenceEnabled(true)

    }
}