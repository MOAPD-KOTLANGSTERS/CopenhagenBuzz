package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.squareup.picasso.Picasso
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.model.dto.BookmarkEvent
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel.BookmarkViewModel

/**
 * A [FirebaseRecyclerAdapter] for displaying a list of [BookmarkEvent]s in a [RecyclerView].
 *
 * This adapter binds event data (name, type, and image) to the corresponding UI elements.
 * It uses [BookmarkViewModel] to asynchronously load images by event ID.
 *
 * @param options The Firebase query options for retrieving [BookmarkEvent] objects.
 * @param bookmarkViewModel The ViewModel responsible for accessing image data.
 */
class BookmarkAdapter(
    options: FirebaseRecyclerOptions<BookmarkEvent>,
    private val bookmarkViewModel: BookmarkViewModel
) : FirebaseRecyclerAdapter<BookmarkEvent, BookmarkAdapter.ViewHolder>(options) {

    /**
     * A [RecyclerView.ViewHolder] that holds references to the views for each bookmark item.
     *
     * @param view The inflated layout for a single item.
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.image_view_event_photo)
        val titleTextView: TextView = view.findViewById(R.id.text_view_event_name)
        val typeTextView: TextView = view.findViewById(R.id.text_view_event_type)
    }

    /**
     * Inflates the layout for a single bookmark item and creates a [ViewHolder].
     *
     * @param parent The parent [ViewGroup] into which the new view will be added.
     * @param viewType The view type of the new view (unused here).
     * @return A new [ViewHolder] that holds the layout for a bookmark item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bookmark_item_row, parent, false)
        return ViewHolder(view)
    }

    /**
     * Binds the data from a [BookmarkEvent] to the corresponding [ViewHolder].
     *
     * @param holder The [ViewHolder] which should be updated.
     * @param position The position of the item in the adapter.
     * @param model The [BookmarkEvent] object to display.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: BookmarkEvent) {
        holder.titleTextView.text = model.eventName
        holder.typeTextView.text = model.eventType

        // Load image if a valid URL or fallback to hiding the ImageView
        if (model.url != null && model.url != "null") {
            bookmarkViewModel.readImage(model.eventId) { resolvedUrl ->
                Picasso.get().load(resolvedUrl).into(holder.imageView)
                holder.imageView.visibility = View.VISIBLE
            }
        } else {
            holder.imageView.visibility = View.GONE
        }
    }
}
