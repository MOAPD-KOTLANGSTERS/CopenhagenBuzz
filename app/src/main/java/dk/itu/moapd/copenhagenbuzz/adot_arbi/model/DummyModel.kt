package dk.itu.moapd.copenhagenbuzz.adot_arbi.model

import java.util.Date

/**
 * An model class with all parameters to represent a dummy object in the Live View application.
 */
data class DummyModel(
    var eventName: String,
    var type: String,
    var eventDate: Date,
    var description: String,
    var url: String
)