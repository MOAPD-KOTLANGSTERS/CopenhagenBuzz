package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.view

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
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
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.dto.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.dto.EventLocation
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel.AddEventViewModel
import dk.itu.moapd.copenhagenbuzz.adot_arbi.util.CustomDate
import java.util.Calendar
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel.TimeLineViewModel
import java.io.File
import java.time.LocalDate


/**
 * A fragment for adding or editing [Event] objects.
 *
 * This screen allows users to:
 * - Enter event details such as name, location, date, type, and description.
 * - Capture a new photo or pick one from the gallery to associate with the event.
 * - Save a new event or update an existing one (if selected via [TimeLineViewModel]).
 * - Delete an event by shaking the device (triggering [onShake]).
 *
 * Navigation destinations and top/bottom bar visibility are configured via [BaseFragment].
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
                        eventLocation = newEvent.eventLocation,
                        updatedAt = -newEvent.updatedAt,
                    ))
                    timeLineViewModel.setEvent()
                    showSnackBar("Event edited successfully!", binding.root)
                }
            } ?: run { setupUserInput  {
                    addEventViewModel.addEvent(it, cameraURI, requireContext())
                    showSnackBar("Event added successfully!", binding.root)
                }
            }
        }

    }

    /**
     * Populates the UI fields with data from an existing [Event] for editing.
     *
     * @param event The [Event] to edit.
     */
    private fun populateUI(event: Event) {
        binding.editTextEventName.setText(event.eventName)
        binding.editTextEventLocation.setText(event.eventLocation.address)
        binding.editTextDateRange.setText(CustomDate.getDateFromEpoch(event.eventDate))
        binding.editTextEventType.setText(event.eventType)
        binding.editTextEventDescription.setText(event.eventDescription)
        event.eventPhotoURL?.let {
            addEventViewModel.readImage(event.id!!) {
                Picasso.get().load(it).into(binding.eventImage)
            }
        }
    }

    /**
     * Sets up listeners for user inputs such as date picker, image selection,
     * and the event submission button.
     *
     * @param onSuccess Callback triggered with a valid [Event] object on form submission.
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
                            eventPhotoURL = cameraURI.toString(),
                            createdAt = -LocalDate.now().toEpochDay(),
                            updatedAt = -LocalDate.now().toEpochDay(),
                        )
                    }
                )
                activity.navController.navigate(R.id.action_addEventFragment_to_timeLineFragment)
            } catch (e: Exception) {
                showSnackBar("Error: ${e.message}, please try again.", binding.root)
            }

        }

        binding.eventImage.setOnClickListener {
            val options = arrayOf("Take a Photo", "Choose from Gallery")

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Select Option")
            builder.setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        val permission = android.Manifest.permission.CAMERA
                        when {
                            ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED -> {
                                launchCamera()
                            }
                            shouldShowRequestPermissionRationale(permission) -> {
                                showSnackBar("Camera permission is needed to take event photos.", binding.root)
                                requestCameraPermissionLauncher.launch(permission)
                            }
                            else -> {
                                requestCameraPermissionLauncher.launch(permission)
                            }
                        }
                    }
                    1 -> {
                        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            android.Manifest.permission.READ_MEDIA_IMAGES
                        } else {
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        }

                        when {
                            ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED -> {
                                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                                intent.type = "image/*"
                                pickImageLauncher.launch(intent)
                            }

                            shouldShowRequestPermissionRationale(permission) -> {
                                Log.w(TAG, "maybe")
                                requestCameraPermissionLauncher.launch(permission)
                            }

                            else -> {
                                Log.w(TAG, "no")
                                requestCameraPermissionLauncher.launch(permission)
                            }
                        }
                    }

                }
            }
            builder.show()


        }
    }

    /**
     * Handles the result of the image picker intent and displays the selected image.
     */
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.let {
                    cameraURI = it // save for Firebase upload
                    binding.eventImage.setImageURI(it)
                    Log.d(TAG, "Selected image URI: $it")
                }
            }
        }

    /**
     * Handles camera permission request result and launches the camera if granted.
     */
    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                launchCamera()
            } else {
                showSnackBar("Camera permission is required to take pictures.", binding.root)
            }
        }


    /**
     * Launches the device's camera app to capture a new image for the event.
     * Saves the URI for later upload.
     */
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

    /**
     * Creates a temporary image file to store the captured photo.
     *
     * @return A [File] pointing to the created image location.
     */
    private fun createImageFile(): File {
        val storageDir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        // Use a [LocalDate] timestamp to ensure unique name
        return File.createTempFile("JPEG_${LocalDate.now()}_", ".jpg", storageDir)
    }

    /**
     * Handles the result of the camera intent and displays the captured image.
     */
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            cameraURI?.let { uri ->
                Picasso.get().load(uri).into(binding.eventImage)
                Log.d(TAG, "Image URI: $uri")
            } ?: Log.e(TAG, "Photo URI was null")
        }
    }


    /**
     * Displays a [Snackbar] with a brief message.
     *
     * @param message The message to show.
     * @param view The anchor view for the [Snackbar].
     */
    private fun showSnackBar(message: String, view: View) {
        Snackbar.make(
            view, message, Snackbar.LENGTH_SHORT
        ).show()
    }

    /**
     * Called when the device is shaken. Prompts the user to confirm deletion
     * of the currently selected event.
     */
    override fun onShake() {
        showSnackBar("Successfully deleted your event", binding.root)
        val event = timeLineViewModel.selectedEvent.value
        event?.let {
            AlertDialog.Builder(requireContext())
                .setTitle("Do you want to delete the event?")
                .setItems(arrayOf("Yes, continue","No")) { _, which ->
                    if (which == 0)
                        addEventViewModel.deleteEvent(event){
                            activity.navController.navigate(R.id.action_addEventFragment_to_timeLineFragment)
                        }
                }.show()
        }
    }
}
