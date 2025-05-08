package dk.itu.moapd.copenhagenbuzz.adot_arbi.util

import android.app.AlertDialog
import android.content.Context
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.util.CustomDate.getDateFromEpoch

/**
 * Utility class that shows a dialog displaying the details of an [Event].
 *
 * @property context The context used to show the [AlertDialog].
 * @property event The event whose details will be displayed.
 */
class ShowEventDetails(private val context: Context, private val event: Event) {

    /**
     * Displays an [AlertDialog] with detailed information about the event.
     */
    fun show() {
        val message = """
            Event: ${event.eventName}
            Type: ${event.eventType}
            Date: ${getDateFromEpoch(event.eventDate)}
            Location: ${event.eventLocation.address}
            Description: ${event.eventDescription}
        """.trimIndent()

        AlertDialog.Builder(context)
            .setTitle("Event Details")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
