package dk.itu.moapd.copenhagenbuzz.adot_arbi.viewModel

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import dk.itu.moapd.copenhagenbuzz.adot_arbi.MainActivity
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentAddEventBinding
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.util.CustomDate
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.Temporal
import java.time.temporal.TemporalAccessor
import java.util.Calendar
import java.util.Date

/**
 * A simple [BaseFragment] subclass for initializing with options for adding
 * a top-bar and bottom-bar
 */
class AddEventFragment : BaseFragment<FragmentAddEventBinding>(
    FragmentAddEventBinding::inflate,
    R.id.action_addEventFragment_to_timeLineFragment,
    R.id.action_addEventFragment_to_bookmarksFragment,
    R.id.action_addEventFragment_to_calenderFragment,
    R.id.action_addEventFragment_to_mapsFragment,
    R.id.action_addEventFragment_self
) {
    companion object {
        private val TAG = MainActivity::class.qualifiedName
    }

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
                    val startDate = CustomDate.of(startDay, startMonth, startYear)
                    val startDateCalendar = Calendar.getInstance()
                    startDateCalendar.set(startYear, startMonth, startDay)
                   editTextDateRange.setText(startDate)
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
                        eventDate = CustomDate.getEpochFromString(editTextDateRange.text.toString()),
                        eventType = editTextEventType.text.toString(),
                        eventDescription = editTextEventDescription.text.toString(),
                        userId = FirebaseAuth.getInstance().currentUser!!.uid
                    )
                }
                message = e.toString()
            }  catch (e: IllegalArgumentException) {
                message = "Error: ${e.message.toString()}"
            } finally {
                message?.let {
                    showMessage(it)
                } ?: run {
                    showMessage("You are not supposed to see this error! Feature not a bug")
                }
            }
        }

    }

    /**
     *  Will toast a message for the user.
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