package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository

import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event

class EventRepository : BaseRepository<Event>(Event::class.java,"event")