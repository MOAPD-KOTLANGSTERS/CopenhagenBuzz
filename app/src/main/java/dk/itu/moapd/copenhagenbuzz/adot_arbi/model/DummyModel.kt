package dk.itu.moapd.copenhagenbuzz.adot_arbi.model

/**
 * An model class with all parameters to represent a dummy object in the Live View application.
 */

data class DummyModel(
    var cityName: String,
    var zipCode: String,
    var country: String,
    var description: String,
    var url: String
)