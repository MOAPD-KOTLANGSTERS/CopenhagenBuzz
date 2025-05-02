package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model

import dk.itu.moapd.copenhagenbuzz.adot_arbi.util.CustomDate
import java.time.Instant
import java.util.Date

/**
 *  A representation of a created event
 *  @param eventName Name of the event
 *  @param eventLocation Location of the event
 *  @param eventDate a Range of 2 dates
 *  @param eventType Type of the event
 *  @param eventDescription A short description of the event
 */
data class Event(
    var id: String? = null,
    var eventName: String,
    var eventLocation: String,
    var eventDate: Long,
    var eventType: String,
    var eventDescription: String,
    var userId: String
) {

    // TODO: change type to enum & date to dateTime
    /**
     * Initial requirements for initialising an Event Class
     */
    init {
        require(eventName.isNotEmpty()) { "Missing Event Name" }
        require(eventLocation.isNotEmpty()) { "Missing Event Location" }
        require(eventDescription.isNotEmpty()) { "Missing Event Description" }
        require(userId.isNotEmpty()) { "Missing user ID" }
    }

    /**
     * Custom toString method for logging
     */
    override fun toString(): String {
        return "Event(eventName='$eventName', eventLocation='$eventLocation', eventDate='${CustomDate.getDateFromEpoch(eventDate)}')"
    }
}