package dk.itu.moapd.copenhagenbuzz.adot_arbi

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // A set of private constants used in this class.
    companion object {
        private val TAG = MainActivity::class.qualifiedName
    }

    // GUI variables.
    private lateinit var eventName: EditText
    private lateinit var eventLocation: EditText
    private lateinit var eventType: EditText
    private lateinit var eventDate: EditText
    private lateinit var eventDescription: EditText
    private lateinit var addEventButton: FloatingActionButton


    // An instance of the `Event` class.
    private val event: Event = Event("", "", "", "", "")


    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window , false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editTextDateRange = findViewById<TextInputEditText>(R.id.edit_text_date_range)

        editTextDateRange.setOnClickListener {
            val calendar = Calendar.getInstance()

            // First, pick the start date
            val startDatePicker = DatePickerDialog(this, { _, startYear, startMonth, startDay ->
                val startDate = "$startDay/${startMonth + 1}/$startYear"

                // Then, pick the end date
                val endDatePicker = DatePickerDialog(this, { _, endYear, endMonth, endDay ->
                    val endDateCalendar = Calendar.getInstance()
                    endDateCalendar.set(endYear, endMonth, endDay)

                    val startDateCalendar = Calendar.getInstance()
                    startDateCalendar.set(startYear, startMonth, startDay)

                    if (endDateCalendar.before(startDateCalendar)) {
                        // If the end date is before the start date, show a warning or reset to start date
                        editTextDateRange.setText("$startDate - $startDate")
                    } else {
                        val endDate = "$endDay/${endMonth + 1}/$endYear"
                        editTextDateRange.setText("$startDate - $endDate")
                    }
                }, startYear, startMonth, startDay)

                endDatePicker.show()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

            startDatePicker.show()


            eventName = findViewById(R.id.edit_text_event_name)
            eventLocation = findViewById(R.id.edit_text_event_location)
            eventDate = findViewById(R.id.edit_text_date_range)
            eventType = findViewById(R.id.edit_text_event_type)
            eventDescription = findViewById(R.id.edit_text_event_description)

            addEventButton = findViewById(R.id.fab_add_event)

// Listener for user interaction in the `Add Event` button.
            addEventButton.setOnClickListener {

                // Only execute the following code when the user fills all `EditText`.
                if (eventName.text.toString().isNotEmpty() &&
                    eventLocation.text.toString().isNotEmpty() &&
                    eventDate.text.toString().isNotEmpty() &&
                    eventType.text.toString().isNotEmpty() &&
                    eventDescription.text.toString().isNotEmpty()
                    ) {

                    // Update the object attributes.
                    event.setEventName(
                        eventName.text.toString().trim()
                    )
                    event.setEventLocation(
                        eventLocation.text.toString().trim()
                    )

                    event.setEventDate(
                        eventDate.text.toString().trim()
                    )

                    event.setEventType(
                        eventType.text.toString().trim()
                    )

                    event.setEventDescription(
                        eventDescription.text.toString().trim()
                    )



                    showMessage()

                }
            }


        }
    }
    private fun showMessage() {
        Log.d(TAG, event.toString())
    }
}