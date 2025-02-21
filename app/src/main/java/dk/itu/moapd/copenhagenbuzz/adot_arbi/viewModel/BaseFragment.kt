package dk.itu.moapd.copenhagenbuzz.adot_arbi.viewModel

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import dk.itu.moapd.copenhagenbuzz.adot_arbi.LoginActivity
import dk.itu.moapd.copenhagenbuzz.adot_arbi.MainActivity
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R


/**
 * A simple [Fragment] subclass.
 * Use the [BaseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
abstract class BaseFragment<VB : ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB,
    private var timelineAction: Int,
    private var bookmarkAction: Int,
    private var calenderAction: Int
) : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = bindingInflater(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.root?.findViewById<ImageButton>(R.id.image_button_logout).let { button ->
            with (button!!){
                if (requireActivity().intent.getBooleanExtra("isLoggedIn", false)) {
                    setImageResource(R.drawable.outline_account_circle_24)
                } else {
                    setImageResource(R.drawable.outline_arrow_back_24)
                }
                setOnClickListener {
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
        setupBottomNav(timelineAction, bookmarkAction, calenderAction)
    }

    /**
     * EVERY SUBCLASS MUST USE THIS!!
     * //TODO Fix this Docu
     */
    private fun setupBottomNav(
        timelineAction: Int,
        bookmarkAction: Int,
        calenderAction: Int
    ) {
        _binding?.root?.findViewById<BottomNavigationView>(R.id.shared_bottom_nav_bar)?.setOnItemSelectedListener { menuItem ->
            val destination = when (menuItem.itemId) {
                R.id.timeline -> timelineAction
                R.id.bookmarks -> bookmarkAction
                R.id.calender -> calenderAction
                else -> return@setOnItemSelectedListener false
            }
            val activity = requireActivity() as MainActivity
            activity.navController.navigate(destination)
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
}