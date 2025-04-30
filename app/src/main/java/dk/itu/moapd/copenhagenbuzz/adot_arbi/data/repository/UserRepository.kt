package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dk.itu.moapd.copenhagenbuzz.adot_arbi.util.DotEnvManager

object UserRepository {
    private val DATABASE_URL = DotEnvManager.dotenv!!["DATABASE_URL"]
    private val db = Firebase.database(DATABASE_URL).reference.child("user")
    init {
        db.keepSynced(true)
    }

}