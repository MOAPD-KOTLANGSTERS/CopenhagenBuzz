package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.interfaces

interface IUserServices {
    // user specific
    fun createUser()
    fun deleteUser()

    // bookmark specific
    fun createFavorite(eventId: String)
    fun readAllFavoriteEvents()
    fun deleteFavoriteEvent(eventId: String)
}