package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.EventServices
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.UserServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookmarkViewModel : ViewModel() {
    fun cleanupInvalidEvent(){
        viewModelScope.launch(Dispatchers.IO) {
            UserServices.readAllFavoriteEvents().forEach {
                withContext(Dispatchers.Main) {
                    Log.d(BookmarkViewModel::class.qualifiedName, it.toString())
                }
            }
        }
    }
}