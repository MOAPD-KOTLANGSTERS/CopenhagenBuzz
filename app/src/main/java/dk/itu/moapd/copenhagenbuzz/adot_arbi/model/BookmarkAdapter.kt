package dk.itu.moapd.copenhagenbuzz.adot_arbi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.DummyModel

class BookmarkAdapter(private val Bookmarks: List<DummyModel>) :
    RecyclerView.Adapter<BookmarkAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.image_view_event_photo)
        val titleTextView: TextView = view.findViewById(R.id.text_view_event_name)
        val typeTextView: TextView = view.findViewById(R.id.text_view_event_type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bookmark_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = Bookmarks[position]
        holder.titleTextView.text = event.eventName
        holder.typeTextView.text = event.type
        Picasso.get().load(event.url).into(holder.imageView)
    }

    override fun getItemCount(): Int = Bookmarks.size
}