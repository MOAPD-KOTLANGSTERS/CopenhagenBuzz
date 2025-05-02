package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.interfaces

interface IUserServices {
    // user specific
    fun createUser()
    fun deleteUser()

    // bookmark specific
    fun createNewFavorite()
    fun readAllFavoriteEvents()
    fun deleteFavoriteEvent()
}