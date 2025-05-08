package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentMapsBinding
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel.MapsViewModel
import dk.itu.moapd.copenhagenbuzz.adot_arbi.util.ShowEventDetails

/**
 * A fragment subclass of [BaseFragment] that displays events on a Google Map.
 *
 * Users can view event markers, click them to show event details, and
 * optionally view their current location if location permission is granted.
 */
class MapsFragment : BaseFragment<FragmentMapsBinding>(
    FragmentMapsBinding::inflate,
    R.id.action_mapsFragment_to_timeLineFragment,
    R.id.action_mapsFragment_to_bookmarksFragment,
    R.id.action_mapsFragment_to_calenderFragment,
    R.id.action_mapsFragment_self,
    R.id.action_mapsFragment_to_addEventFragment,
), OnMapReadyCallback {

    /**
     * The [MapView] used to render the Google Map.
     */
    private lateinit var mapView: MapView

    /**
     * Instance of the [GoogleMap] object once it’s ready.
     */
    private lateinit var googleMap: GoogleMap

    /**
     * Shared ViewModel that provides access to the list of events.
     */
    private val mapsViewModel: MapsViewModel by activityViewModels()

    /**
     * Initializes the map view when the fragment’s view is created.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    /**
     * Called when the GoogleMap is ready to be used.
     * Adds markers and observes event data from the ViewModel.
     */
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        setupMap()

        // Observe events and display markers
        mapsViewModel.getAllEvents()
        mapsViewModel.events.observe(viewLifecycleOwner) { events ->
            fetchAndDisplayEvents(events)
        }
    }

    /**
     * Displays event markers on the map and focuses the camera on the first event.
     *
     * @param events A list of [Event] objects to display on the map.
     */
    private fun fetchAndDisplayEvents(events: List<Event>) {
        // Add markers for each event
        events.forEach { event ->
            val location = LatLng(event.eventLocation.lat, event.eventLocation.long)
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(event.eventName)
                    .snippet(event.eventDescription)
            )
            marker?.tag = event
        }

        // Focus camera on the first event
        if (events.isNotEmpty()) {
            val firstEvent = events[0]
            val firstLatLng = LatLng(firstEvent.eventLocation.lat, firstEvent.eventLocation.long)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 10f))
        }

        // Handle marker clicks to show event details
        googleMap.setOnMarkerClickListener { marker ->
            val event = marker.tag as? Event
            event?.let {
                val showEventDetails = ShowEventDetails(requireContext(), it)
                showEventDetails.show()
            }
            true
        }
    }

    /**
     * Configures map settings and requests location permissions if not already granted.
     */
    private fun setupMap() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isZoomControlsEnabled = true
        } else {
            requestUserPermissions()
        }
    }

    /**
     * Requests location permissions from the user.
     */
    private fun requestUserPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
        )
    }

    /**
     * Resumes the [MapView] when the fragment becomes active.
     */
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    /**
     * Pauses the [MapView] when the fragment is no longer in the foreground.
     */
    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    /**
     * Displays a long toast message.
     *
     * @param message The message to display.
     */
    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    /**
     * Destroys the [MapView] when the fragment view is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }
}
