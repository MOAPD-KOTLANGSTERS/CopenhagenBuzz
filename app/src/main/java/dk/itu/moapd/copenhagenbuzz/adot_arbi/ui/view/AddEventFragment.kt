package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.view

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
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
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel.TimeLineViewModel
import java.io.File
import java.time.LocalDate


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
        private val CAMERA_PERMISSION_CODE = 1001
    }

    private val timeLineViewModel: TimeLineViewModel by activityViewModels()
    private val addEventViewModel: AddEventViewModel by viewModels()

    private var cameraURI : Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timeLineViewModel.selectedEvent.observe(viewLifecycleOwner) { event ->
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
                timeLineViewModel.setEvent()
            } ?: run { setupUserInput  {
                    addEventViewModel.addEvent(it, cameraURI, requireContext())
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
        event.eventPhotoURL?.let { Picasso.get().load(it).into(binding.eventImage) }
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
                            userId = FirebaseAuth.getInstance().currentUser!!.uid,
                            eventPhotoURL = cameraURI.toString()
                        )
                    }
                )
                activity.navController.navigate(R.id.action_addEventFragment_to_timeLineFragment)
            } catch (e: Exception) {
                showSnackBar("Error: ${e.message}, please try again.", binding.root)
            }

        }

        binding.eventImage.setOnClickListener {
            Log.d(TAG, "CLICKED")
            launchCamera()
        }

    }

    private fun launchCamera() {
        try {
            val photoFile = createImageFile()
            cameraURI = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                photoFile
            )
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, cameraURI)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
            cameraLauncher.launch(cameraIntent)
        } catch (e: Exception) {
            showSnackBar("Failed to launch camera: ${e.message}", binding.root)
            Log.e(TAG, "Camera launch error", e)
        }
    }

    private fun createImageFile(): File {
        val timeStamp = LocalDate.now()
        val storageDir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }


    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            cameraURI?.let { uri ->
                Picasso.get().load(uri).into(binding.eventImage)
                Log.d(TAG, "Image URI: $uri")
            } ?: Log.e(TAG, "Photo URI was null")
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
