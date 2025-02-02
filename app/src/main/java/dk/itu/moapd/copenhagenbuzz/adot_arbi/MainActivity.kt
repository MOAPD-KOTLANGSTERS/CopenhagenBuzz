package dk.itu.moapd.copenhagenbuzz.adot_arbi

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
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
                    val endDate = "$endDay/${endMonth + 1}/$endYear"
                    editTextDateRange.setText("$startDate - $endDate")
                }, startYear, startMonth, startDay)

                endDatePicker.show()
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

            // TODO: add safety for not picking a date before first date

            startDatePicker.show()
        }

    }
}