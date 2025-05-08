import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import java.util.Locale
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Converts a human-readable address string into geographic coordinates (latitude and longitude).
 *
 * On Android 13+ (TIRAMISU), this uses the asynchronous [Geocoder.GeocodeListener].
 * For lower versions, it uses a blocking call.
 *
 * @param context The Android context used to access the [Geocoder].
 * @param address The full address to geocode.
 * @return A [Pair] of (latitude, longitude) or `null` if the address can't be resolved.
 */
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

/**
 * Converts geographic coordinates (latitude and longitude) into a human-readable address string.
 *
 * On Android 13+ (TIRAMISU), this uses the asynchronous [Geocoder.GeocodeListener].
 * For lower versions, it uses a blocking call.
 *
 * @param context The Android context used to access the [Geocoder].
 * @param latitude The latitude of the location.
 * @param longitude The longitude of the location.
 * @return A formatted address string or `null` if no address is found.
 */
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
