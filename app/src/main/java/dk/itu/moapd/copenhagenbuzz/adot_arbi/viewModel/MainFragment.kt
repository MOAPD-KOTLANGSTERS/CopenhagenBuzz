package dk.itu.moapd.copenhagenbuzz.adot_arbi.viewModel

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentMainBinding
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.Event
import java.util.Calendar



/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : BaseFragment<FragmentMainBinding>(
    FragmentMainBinding::inflate,
    R.id.action_mainFragment_to_timeLineFragment3,
    R.id.action_mainFragment_to_bookmarksFragment3,
    R.id.action_mainFragment_to_calenderFragment2
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUserInput()
    }


    /**
     * Sets up event listeners for UI elements.
     * Includes date range selection and event creation.
     */
    private fun setupUserInput() {

        val editTextDateRange = binding.editTextDateRange

        // Listener for date range selection.
        editTextDateRange.setOnClickListener {
            val calendar = Calendar.getInstance()

            // Opens the start date picker dialog.
            val startDatePicker = DatePickerDialog(
                requireContext(),
                { _, startYear, startMonth, startDay ->
                    val startDate = "$startDay/${startMonth + 1}/$startYear"

                    // Opens the end date picker dialog after selecting the start date.
                    val endDatePicker =
                        DatePickerDialog(requireContext(), { _, endYear, endMonth, endDay ->
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
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

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
}