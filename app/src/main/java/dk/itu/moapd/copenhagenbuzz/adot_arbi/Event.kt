package dk.itu.moapd.copenhagenbuzz.adot_arbi

class Event {

    private var eventName: String
    private var eventLocation: String
    private var eventDate: String
    private var eventType: String
    private var eventDescription: String

    constructor(eventName: String, eventLocation: String, eventDate: String, eventType: String, eventDescription: String) {
        this.eventName = eventName
        this.eventLocation = eventLocation
        this.eventDate = eventDate
        this.eventType = eventType
        this.eventDescription = eventDescription
    }

    fun getEventName(): String {
        return eventName
    }

    fun setEventName(eventName: String) {
        this.eventName = eventName
    }

    fun getEventLocation(): String {
        return eventLocation
    }

    fun setEventLocation(eventLocation: String) {
        this.eventLocation = eventLocation
    }

    fun getEventDate(): String {
        return eventDate
    }

    fun setEventDate(eventDate: String) {
        this.eventDate = eventDate
    }

    fun getEventType(): String {
        return eventType
    }

    fun setEventType(eventType: String) {
        this.eventType = eventType
    }

    fun getEventDescription(): String {
        return eventDescription
    }

    fun setEventDescription(eventDescription: String) {
        this.eventDescription = eventDescription
    }


    override fun toString(): String {
        return "Event(eventName='$eventName', eventLocation='$eventLocation')"
    }
}