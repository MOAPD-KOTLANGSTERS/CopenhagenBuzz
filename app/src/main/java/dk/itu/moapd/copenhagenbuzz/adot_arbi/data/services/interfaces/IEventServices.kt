package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.interfaces

interface IEventServices {
    fun createEvent()
    fun readAllEvents()
    fun updateEvent()
    fun deleteEvent()
}