package dk.itu.moapd.copenhagenbuzz.adot_arbi.viewModel

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dk.itu.moapd.copenhagenbuzz.adot_arbi.LoginActivity
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentMainBinding


/**
 * A simple [Fragment] subclass.
 * Use the [BaseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
abstract class BaseFragment : Fragment() {

    abstract override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View


    /**
     * EVERY SUBCLASS MUST USE THIS!!
     * //TODO Fix this Docu
     */
    protected fun setupBottomNav(
        bottomNavView: BottomNavigationView,
        navController: NavController,
        timelineAction: Int,
        bookmarkAction: Int,
        calenderAction: Int
    ) {
        bottomNavView.setOnItemSelectedListener { menuItem ->
            val destination = when (menuItem.itemId) {
                R.id.timeline -> timelineAction
                R.id.bookmarks -> bookmarkAction
                R.id.calender -> calenderAction
                else -> return@setOnItemSelectedListener false
            }
            navController.navigate(destination)
            true
        }
    }
}