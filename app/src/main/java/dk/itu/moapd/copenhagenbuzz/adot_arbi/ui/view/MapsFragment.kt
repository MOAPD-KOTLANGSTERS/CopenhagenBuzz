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
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.DummyModel
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentMapsBinding
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel.MapsViewModel

class MapsFragment : BaseFragment<FragmentMapsBinding>(
    FragmentMapsBinding::inflate,
    R.id.action_mapsFragment_to_timeLineFragment,
    R.id.action_mapsFragment_to_bookmarksFragment,
    R.id.action_mapsFragment_to_calenderFragment,
    R.id.action_mapsFragment_self,
    R.id.action_mapsFragment_to_addEventFragment,
), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private val dataViewModel: MapsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        setupMap()

        // Observe events from DataViewModel
        dataViewModel.events.observe(viewLifecycleOwner) { events ->
            fetchAndDisplayEvents(events)
        }

    }

    private fun fetchAndDisplayEvents(events: List<Event>) {
        // Add markers for each event
        events.forEach { event ->
            val location = LatLng(0.0, 0.0)
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
            val firstLatLng = LatLng(0.0, 0.0)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 10f))
        }

        // Handle marker clicks
        googleMap.setOnMarkerClickListener { marker ->
            val event = marker.tag as? DummyModel
            event?.let {
                showEventDetails(it)
            }
            true
        }
    }

    private fun showEventDetails(event: DummyModel) {
        val message = "Event: ${event.eventName}\nLocation: ${event.location.address}\nDescription: ${event.description}"
        showMessage(message)
    }

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



    private fun requestUserPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
        )
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }
}