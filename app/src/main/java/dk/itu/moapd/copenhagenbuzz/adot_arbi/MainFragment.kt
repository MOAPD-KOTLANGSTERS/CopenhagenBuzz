package dk.itu.moapd.copenhagenbuzz.adot_arbi

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentMainBinding
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.Event
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentMainBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUserInput()
    }

    /*
    private fun setupLogout() {
        val button = binding.materialButtonLogout
        // Change the buttons depending on verified login
        if (intent.getBooleanExtra("isLoggedIn", false)) {
            button.setImageResource(R.drawable.outline_account_circle_24)
        } else {
            button.setImageResource(R.drawable.outline_arrow_back_24)
        }

        button.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
    * */

    /**
     * Sets up event listeners for UI elements.
     * Includes date range selection and event creation.
     */
    private fun setupUserInput() {
        binding.materialButtonLogout.setImageResource(R.drawable.outline_account_circle_24)
        val editTextDateRange = binding.editTextDateRange

        // Listener for date range selection.
        editTextDateRange.setOnClickListener {
            val calendar = Calendar.getInstance()

            // Opens the start date picker dialog.
            val startDatePicker = DatePickerDialog(requireContext(), { _, startYear, startMonth, startDay ->
                val startDate = "$startDay/${startMonth + 1}/$startYear"

                // Opens the end date picker dialog after selecting the start date.
                val endDatePicker = DatePickerDialog(requireContext(), { _, endYear, endMonth, endDay ->
                    val endDateCalendar = Calendar.getInstance()
                    endDateCalendar.set(endYear, endMonth, endDay)

                    val startDateCalendar = Calendar.getInstance()
                    startDateCalendar.set(startYear, startMonth, startDay)

                    // Ensures the end date is not before the start date.
                    if (endDateCalendar.before(startDateCalendar)) {
                        // If invalid, set both dates to the start date.
                        editTextDateRange.setText("$startDate - $startDate")
                    } else {
                        val endDate = "$endDay/${endMonth + 1}/$endYear"
                        editTextDateRange.setText("$startDate - $endDate")
                    }
                }, startYear, startMonth, startDay)

                endDatePicker.show()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

            startDatePicker.show()
        }
        
        // Listener for the "Add Event" button to create a new event.
        binding.fabAddEvent.setOnClickListener {
            var message : String? = null
            try {
                val e = with(binding) {
                    Event(
                        eventName = editTextEventName.text.toString(),
                        eventLocation = editTextEventLocation.text.toString(),
                        eventDate = editTextDateRange.text.toString(),
                        eventType = editTextEventType.text.toString(),
                        eventDescription = editTextEventDescription.text.toString()
                    )
                }
                message = e.toString()
            }  catch (e: IllegalArgumentException) {
                message = "Error: ${e.message.toString()}"
            } finally {
                message?.let {
                    showMessage(it)
                }
            }
        }
    }

    /**
     *  Displays a message in the log of the device.
     *
     *  @param s String message
     */
    private fun showMessage(s: String){
        val log = binding.log
        log.text = s

        log.visibility = View.VISIBLE
        log.alpha = 1f

        // Delay for 2 seconds (2000 milliseconds), then fade out
        Handler(Looper.getMainLooper()).postDelayed({
            log.animate()
                .alpha(0f)  // Fade to invisible
                .setDuration(500) // Animation lasts 1 second
                .withEndAction { log.visibility = View.INVISIBLE } // Remove from layout
        }, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}