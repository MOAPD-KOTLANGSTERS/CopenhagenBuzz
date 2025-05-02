package dk.itu.moapd.copenhagenbuzz.adot_arbi.util

import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Coroutine {
    fun suspendLaunch(callback : suspend () -> Unit) = CoroutineScope(Dispatchers.IO).launch { callback() }
    fun launch(callback : () -> Unit) = CoroutineScope(Dispatchers.IO).launch { callback() }
    
}