package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentMapsBinding
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.dto.Event
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
) {

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

        mapView.getMapAsync { map ->
            googleMap = map
            setupMap()


            /**
             * Observe the events from the ViewModel and set up markers on the map.
             * Clear existing markers before adding new ones.
             */

            mapsViewModel.getAllEvents()
            mapsViewModel.events.observe(viewLifecycleOwner) { events ->
                googleMap.clear() // Clear existing markers
                mapsViewModel.setupMap(googleMap) // Add markers for events

                // Zoom to the last event if available
                events.lastOrNull()?.let { lastEvent ->
                    val lastEventLocation = LatLng(lastEvent.eventLocation.lat, lastEvent.eventLocation.long)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastEventLocation, 15f))
                }
            }

            /**
             * Set up a click listener for the map to show event details when a marker is clicked.
             * The event data is stored in the marker's tag.
             */
            // Handle marker click to show event details
            googleMap.setOnMarkerClickListener { marker ->
                val event = marker.tag as? Event
                event?.let {
                    ShowEventDetails(requireContext(), it).show()
                }
                true
            }
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
     * Destroys the [MapView] when the fragment view is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }
}