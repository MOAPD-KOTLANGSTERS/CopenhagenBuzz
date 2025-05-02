// GeoUtils.kt
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
suspend fun getCoordinatesFromAddress(context: Context, address: String): Pair<Double, Double>? =
    suspendCancellableCoroutine { continuation ->
        val geocoder = Geocoder(context, Locale.getDefault())
        geocoder.getFromLocationName(address, 1, @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        object : Geocoder.GeocodeListener {
            override fun onGeocode(results: MutableList<Address>) {
                if (results.isNotEmpty()) {
                    val loc = results[0]
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

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
suspend fun getAddressFromCoordinates(context: Context, latitude: Double, longitude: Double): String? =
    suspendCancellableCoroutine { continuation ->
        val geocoder = Geocoder(context, Locale.getDefault())
        geocoder.getFromLocation(latitude, longitude, 1, @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        object : Geocoder.GeocodeListener {
            override fun onGeocode(results: MutableList<Address>) {
                if (results.isNotEmpty()) {
                    continuation.resume(results[0].getAddressLine(0))
                } else {
                    continuation.resume(null)
                }
            }

            override fun onError(errorMessage: String?) {
                continuation.resume(null)
            }
        })
    }
