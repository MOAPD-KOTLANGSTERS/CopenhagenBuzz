package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model

data class User(
    val uuid: String = "",
    val favorites : Map<String, BookmarkEvent> = emptyMap(),
)