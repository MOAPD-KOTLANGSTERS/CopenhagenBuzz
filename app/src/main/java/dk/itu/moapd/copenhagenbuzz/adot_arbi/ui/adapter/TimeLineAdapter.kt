package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.firebase.ui.database.FirebaseListAdapter
import com.firebase.ui.database.FirebaseListOptions
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.view.MainActivity
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel.TimeLineViewModel
import dk.itu.moapd.copenhagenbuzz.adot_arbi.util.CustomDate

/**
 * A [FirebaseListAdapter] for displaying [Event] objects in a `ListView`.
 * Each item includes the event name, type, date, description, photo, and buttons for
 * favoriting and editing.
 *
 * @param context The [Context] used to inflate views.
 * @param resource The layout resource ID for an individual event item.
 * @param timeLineViewModel ViewModel handling event-related operations.
 * @param mainActivity The activity used for navigation to the edit screen.
 * @param options The Firebase configuration options for the adapter.
 */
class TimeLineAdapter(
    private val context: Context,
    private var resource: Int,
    private val timeLineViewModel: TimeLineViewModel,
    private val mainActivity: MainActivity,
    options: FirebaseListOptions<Event>,
) : FirebaseListAdapter<Event>(options) {

    companion object {
        private val TAG = TimeLineAdapter::class.qualifiedName
    }

    /**
     * A view holder to cache views for each event item in the list.
     *
     * @param view The view representing the item layout.
     */
    private inner class ViewHolder(view: View) {
        val imageViewPhoto: ImageView = view.findViewById(R.id.image_view_photo)
        val textViewTitle: TextView = view.findViewById(R.id.text_view_title)
        val textViewSubtitle: TextView = view.findViewById(R.id.text_view_subtitle)
        val textViewDescription: TextView = view.findViewById(R.id.text_view_description)
        val buttonFavorite: MaterialButton = view.findViewById(R.id.button_favorite)
        val buttonEdit: Button = view.findViewById(R.id.button_edit)
    }

    /**
     * Returns a view that displays the data at the specified position in the data set.
     *
     * @param position The position of the item.
     * @param convertView A recycled view for reuse.
     * @param parent The parent view group.
     * @return A view corresponding to the data at the specified position.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
        val viewHolder = (view.tag as? ViewHolder) ?: ViewHolder(view)

        populateView(view, getItem(position), position)

        view.tag = viewHolder
        return view
    }

    /**
     * Populates a single event item's view with data and sets up interaction handlers.
     *
     * @param v The view to populate.
     * @param model The [Event] model to bind.
     * @param position The position in the list.
     */
    override fun populateView(v: View, model: Event, position: Int) {
        with(ViewHolder(v)) {
            // Load image if valid
            if (model.eventPhotoURL != null && model.eventPhotoURL != "null") {
                timeLineViewModel.readImage(model.id!!) {
                    Picasso.get().load(it).into(imageViewPhoto)
                    imageViewPhoto.visibility = View.VISIBLE
                }
            } else {
                imageViewPhoto.visibility = View.GONE
            }

            // Update favorite icon
            checkIfFavorite(model, buttonFavorite)

            // Set basic event details
            textViewTitle.text = model.eventName
            textViewSubtitle.text = context.getString(
                R.string.secondary_text,
                model.eventType,
                CustomDate.getDateFromEpoch(model.eventDate)
            )
            textViewDescription.text = model.eventDescription

            // Handle favorite toggle
            buttonFavorite.setOnClickListener {
                timeLineViewModel.addFavorite(model.id!!) { result ->
                    toggleFavorite(buttonFavorite, result)
                }
            }

            // Show edit button if the current user is the creator
            FirebaseAuth.getInstance().currentUser?.let {
                if (it.uid == model.userId) {
                    buttonEdit.visibility = View.VISIBLE
                }
            }

            // Navigate to edit screen
            buttonEdit.setOnClickListener {
                timeLineViewModel.selectedEvent.postValue(model)
                mainActivity.navController.navigate(R.id.action_timeLineFragment_to_addEventFragment)
            }
        }
    }

    /**
     * Checks if an event is favorited by the current user and updates the button UI accordingly.
     *
     * @param model The event to check.
     * @param buttonFavorite The button to toggle.
     */
    private fun checkIfFavorite(model: Event, buttonFavorite: MaterialButton) {
        model.id?.let { id ->
            timeLineViewModel.isFavorite(id) { result ->
                toggleFavorite(buttonFavorite, result)
            }
        }
    }

    /**
     * Toggles the favorite icon on the button based on whether the event is favorited.
     *
     * @param buttonFavorite The button to update.
     * @param value `true` if favorited, `false` otherwise.
     */
    private fun toggleFavorite(buttonFavorite: MaterialButton, value: Boolean) {
        if (value) {
            buttonFavorite.setIconResource(R.drawable.baseline_heart_icon_filled)
        } else {
            buttonFavorite.setIconResource(R.drawable.baseline_heart_icon)
        }
    }
}
