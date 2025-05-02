package dk.itu.moapd.copenhagenbuzz.adot_arbi

import android.app.Application
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.adapter.EventAdapter
import dk.itu.moapd.copenhagenbuzz.adot_arbi.util.DotenvManager
import com.google.android.material.color.DynamicColors

class MyApplication : Application() {

    companion object {
        private val TAG = EventAdapter::class.qualifiedName
    }


    override fun onCreate() {
        super.onCreate()
        Firebase.database(DotenvManager.DATABASE_URL).setPersistenceEnabled(true)

        // Apply dynamic colors
        DynamicColors.applyToActivitiesIfAvailable(this)

    }
}