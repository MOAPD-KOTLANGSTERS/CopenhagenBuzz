package dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository

import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.DummyModel
import kotlinx.coroutines.tasks.await

class UserRepository : BaseRepository<DummyModel>(DummyModel::class.java,"user") {
    suspend fun getBookmarks(id: String) = db.child(id).child("bookmarks").get().await()
}