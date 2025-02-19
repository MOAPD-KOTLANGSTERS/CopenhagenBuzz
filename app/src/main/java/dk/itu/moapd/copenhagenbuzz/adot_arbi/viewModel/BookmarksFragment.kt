package dk.itu.moapd.copenhagenbuzz.adot_arbi.viewModel

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import dk.itu.moapd.copenhagenbuzz.adot_arbi.LoginActivity
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentBookmarksBinding

/**
 * A simple [Fragment] subclass.
 * Use the [BookmarksFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookmarksFragment : BaseFragment() {
    private var _binding : FragmentBookmarksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentBookmarksBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.toolbarTop?.materialButtonLogout?.let { button ->
            with (button){
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

        setupBottomNav(
            binding.bottomNavBar.bottomNavBar,
            findNavController(),
            R.id.action_bookmarksFragment_to_timeLineFragment2,
            R.id.action_bookmarksFragment_self,
            R.id.action_bookmarksFragment_to_calenderFragment2
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
