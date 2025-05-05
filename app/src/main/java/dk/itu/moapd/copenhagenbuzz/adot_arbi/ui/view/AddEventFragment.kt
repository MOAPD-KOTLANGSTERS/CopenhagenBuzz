package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.view

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentAddEventBinding
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.EventLocation
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel.AddEventViewModel
import dk.itu.moapd.copenhagenbuzz.adot_arbi.util.CustomDate
import java.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel.TimeLineViewModel


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
        private val TAG = AddEventFragment::class.qualifiedName
    }

    private val timeLineViewModel: TimeLineViewModel by activityViewModels()
    private val addEventViewModel: AddEventViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timeLineViewModel.selectedEvent.observe(viewLifecycleOwner) { event ->
            Log.d(TAG, "OBSERVING")
            event?.let {
                populateUI(it)
                setupUserInput { newEvent ->
                    addEventViewModel.updateEvent(event.copy(
                        eventName = newEvent.eventName,
                        eventDate = newEvent.eventDate,
                        eventDescription = newEvent.eventDescription,
                        eventType = newEvent.eventType,
                        eventLocation = newEvent.eventLocation
                    ))
                    timeLineViewModel.setEvent()
                    showSnackBar("Event edited successfully!", binding.root)
                }
            } ?: run { setupUserInput  {
                    addEventViewModel.addEvent(it, requireContext())
                    showSnackBar("Event added successfully!", binding.root)
                }
            }
        }

    }

    private fun populateUI(event: Event) {
        binding.editTextEventName.setText(event.eventName)
        binding.editTextEventLocation.setText(event.eventLocation.address)
        binding.editTextDateRange.setText(CustomDate.getDateFromEpoch(event.eventDate))
        binding.editTextEventType.setText(event.eventType)
        binding.editTextEventDescription.setText(event.eventDescription)
    }

    /**
     * Sets up event listeners for UI elements.
     * Includes date range selection and event creation.
     */
    private fun setupUserInput(onSuccess: (event : Event) -> Unit) {

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
            // Using a callback here to manage it higher up

            try {
                onSuccess(
                    with(binding) {
                        Event(
                            eventName = editTextEventName.text.toString(),
                            eventLocation = EventLocation(address = editTextEventLocation.text.toString()),
                            eventDate = CustomDate.getEpochFromString(editTextDateRange.text.toString()),
                            eventType = editTextEventType.text.toString(),
                            eventDescription = editTextEventDescription.text.toString(),
                            userId = FirebaseAuth.getInstance().currentUser!!.uid
                        )
                    }
                )
                activity.navController.navigate(R.id.action_addEventFragment_to_timeLineFragment)
            } catch (e: Exception) {
                showSnackBar("Error: ${e.message}, please try again.", binding.root)
            }

        }

    }


    /**
     * Displays a SnackBar to show a brief message about the clicked button.
     *
     * The SnackBar is created using the clicked button and is shown at the bottom of the screen.
     *
     * @param message The message to be displayed in the SnackBar.
     * @param view The view to find a parent from.
     */
    private fun showSnackBar(message: String, view: View) {
        Snackbar.make(
            view, message, Snackbar.LENGTH_SHORT
        ).show()
    }
}
