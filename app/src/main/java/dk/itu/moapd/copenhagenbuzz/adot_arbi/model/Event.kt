package dk.itu.moapd.copenhagenbuzz.adot_arbi.model

/**
 *  A representation of a created event
 *  @param eventName Name of the event
 *  @param eventLocation Location of the event
 *  @param eventDate a Range of 2 dates
 *  @param eventType Type of the event
 *  @param eventDescription A short description of the event
 */
data class Event(var eventName: String,
            var eventLocation: String,
            var eventDate: String,
            var eventType: String,
            var eventDescription: String) {

    // TODO: change type to enum & date to dateTime
    /**
     * Initial requirements for initialising an Event Class
     */
    init {
        require(eventName.isNotEmpty()) { "Missing Event Name" }
        require(eventLocation.isNotEmpty()) { "Missing Event Location" }
        require(eventDate.isNotEmpty()) { "Missing Event Date" }
        require(eventDescription.isNotEmpty()) { "Missing Event Description" }
    }

    /**
     * Secondary constructor for the Event class with default values
     * @param eventName Name of the event
     */
    constructor(eventName: String) : this(eventName, "ITU", "now", "other", "") {
        this.eventName = eventName
    }

    /**
     * Custom toString method for logging
     */
    override fun toString(): String {
        return "Event(eventName='$eventName', eventLocation='$eventLocation')"
    }
}