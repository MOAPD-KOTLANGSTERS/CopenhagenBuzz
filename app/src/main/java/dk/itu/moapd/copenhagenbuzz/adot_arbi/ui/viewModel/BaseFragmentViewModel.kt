package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * A base model to share isDialogShowing for BaseFragment.
 */
class BaseFragmentViewModel : ViewModel() {

    private val _isDialogShowing = MutableLiveData<Boolean>()
    val isDialogShowing: MutableLiveData<Boolean>
        get() = _isDialogShowing

    init {
        _isDialogShowing.value = false
    }

    /**
     * Updates the dialog showing state.
     *
     * @param isShowing `true` if a dialog is showing, `false` otherwise.
     */
    fun setDialogShowing(isShowing: Boolean) {
        _isDialogShowing.value = isShowing
    }
}