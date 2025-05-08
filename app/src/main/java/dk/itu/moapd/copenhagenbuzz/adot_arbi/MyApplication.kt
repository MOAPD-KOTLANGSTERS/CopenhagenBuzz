package dk.itu.moapd.copenhagenbuzz.adot_arbi

import android.app.Application
import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.adapter.TimeLineAdapter
import dk.itu.moapd.copenhagenbuzz.adot_arbi.util.DotenvManager
import com.google.android.material.color.DynamicColors

/**
 * Custom [Application] class used to configure global application-level settings.
 *
 * - Initializes Firebase with persistence enabled.
 * - Applies Material dynamic colors to all activities (if available).
 */
class MyApplication : Application() {
    
    /**
     * Called when the application is starting.
     * Used here to initialize Firebase Realtime Database and enable dynamic theming.
     */
    override fun onCreate() {
        super.onCreate()

        // Enable offline persistence for Firebase Realtime Database
        Firebase.database(DotenvManager.RT_DATABASE_URL).setPersistenceEnabled(true)

        // Apply Material You dynamic colors to all activities, if the feature is supported
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}
