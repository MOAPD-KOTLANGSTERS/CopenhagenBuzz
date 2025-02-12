package dk.itu.moapd.copenhagenbuzz.adot_arbi

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.textfield.TextInputEditText
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.ActivityMainBinding
import java.util.Calendar

/**
 * MainActivity serves as the entry point of the application.
 * It initializes the UI components, binds views using View Binding,
 * and handles user interactions such as date selection and event creation.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding

    /**
     * Called when the activity is first created.
     * Initializes the UI, sets up window insets, and attaches event listeners.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        // Inflate the layout using View Binding and set the content view.
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        // Initialize listeners for user interactions.
        setupListeners()
    }

    /**
     * Sets up event listeners for UI elements.
     * Includes date range selection and event creation.
     */
    private fun setupListeners() {
        val editTextDateRange = mainBinding.contentMain.editTextDateRange

        // Listener for date range selection.
        editTextDateRange.setOnClickListener {
            val calendar = Calendar.getInstance()

            // Opens the start date picker dialog.
            val startDatePicker = DatePickerDialog(this, { _, startYear, startMonth, startDay ->
                val startDate = "$startDay/${startMonth + 1}/$startYear"

                // Opens the end date picker dialog after selecting the start date.
                val endDatePicker = DatePickerDialog(this, { _, endYear, endMonth, endDay ->
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
        mainBinding.contentMain.fabAddEvent.setOnClickListener {
            var message = ""
            try {
                val e = Event(
                    eventName = mainBinding.contentMain.editTextEventName.text.toString(),
                    eventLocation = mainBinding.contentMain.editTextEventLocation.text.toString(),
                    eventDate = mainBinding.contentMain.editTextDateRange.text.toString(),
                    eventType = mainBinding.contentMain.editTextEventType.text.toString(),
                    eventDescription = mainBinding.contentMain.editTextEventDescription.text.toString()
                )
                message = e.toString()
            }  catch (e: IllegalArgumentException) {
                message = "Error: ${e.message.toString()}"
            } finally {
                showMessage(message)
            }
        }
    }

    /**
     *  Displays a message in the log of the device.
     *
     *  @param s String message
     */
    private fun showMessage(s: String){
        val log = mainBinding.contentMain.log
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