import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import java.util.Locale
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

suspend fun getCoordinatesFromAddress(context: Context, address: String): Pair<Double, Double>? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        suspendCancellableCoroutine { continuation ->
            val geocoder = Geocoder(context, Locale.getDefault())
            geocoder.getFromLocationName(address, 1, object : Geocoder.GeocodeListener {
                override fun onGeocode(results: MutableList<Address>) {
                    val loc = results.firstOrNull()
                    if (loc != null) {
                        continuation.resume(Pair(loc.latitude, loc.longitude))
                    } else {
                        continuation.resume(null)
                    }
                }

                override fun onError(errorMessage: String?) {
                    continuation.resume(null)
                }
            })
        }
    } else {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val results = geocoder.getFromLocationName(address, 1)
            results?.firstOrNull()?.let { Pair(it.latitude, it.longitude) }
        } catch (e: Exception) {
            null
        }
    }
}

suspend fun getAddressFromCoordinates(context: Context, latitude: Double, longitude: Double): String? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        suspendCancellableCoroutine { continuation ->
            val geocoder = Geocoder(context, Locale.getDefault())
            geocoder.getFromLocation(latitude, longitude, 1, object : Geocoder.GeocodeListener {
                override fun onGeocode(results: MutableList<Address>) {
                    val addressLine = results.firstOrNull()?.getAddressLine(0)
                    continuation.resume(addressLine)
                }

                override fun onError(errorMessage: String?) {
                    continuation.resume(null)
                }
            })
        }
    } else {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val results = geocoder.getFromLocation(latitude, longitude, 1)
            results?.firstOrNull()?.getAddressLine(0)
        } catch (e: Exception) {
            null
        }
    }
}
