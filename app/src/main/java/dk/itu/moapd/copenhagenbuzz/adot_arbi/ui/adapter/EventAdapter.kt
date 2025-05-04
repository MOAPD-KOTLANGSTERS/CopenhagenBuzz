package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.firebase.ui.database.FirebaseListAdapter
import com.firebase.ui.database.FirebaseListOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.model.Event
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.repository.UserRepository
import dk.itu.moapd.copenhagenbuzz.adot_arbi.data.services.UserServices
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel.TimeLineViewModel
import dk.itu.moapd.copenhagenbuzz.adot_arbi.util.CustomDate

/**
 * A class to customize an adapter with a `ViewHolder` to populate a list of dummy objects in a
 * `ListView`.
 */

/*
 * This code is adapted from moapd2025
 * Original author: Fabricio Batista Narcizo
 * License: MIT
 * Source: https://github.com/fabricionarcizo/moapd2025/blob/main/lecture04/04-4_ListView/app/src/main/java/dk/itu/moapd/listview/CustomAdapter.kt
 */
class EventAdapter(
    private val context: Context,
    private var resource: Int,
    private val isLoggedIn: Boolean,
    private val timeLineViewModel: TimeLineViewModel,
    options: FirebaseListOptions<Event>,
) : FirebaseListAdapter<Event>(options) {

    /**
     * A set of private constants used in this class.
     */
    companion object {
        private val TAG = EventAdapter::class.qualifiedName
    }

    /**
     * An internal view holder class used to represent the layout that shows a single `DummyModel`
     * instance in the `ListView`.
     */
    private inner class ViewHolder(view: View) {
        val imageViewPhoto: ImageView = view.findViewById(R.id.image_view_photo)
        val textViewTitle: TextView = view.findViewById(R.id.text_view_title)
        val textViewSubtitle: TextView = view.findViewById(R.id.text_view_subtitle)
        val textViewDescription: TextView = view.findViewById(R.id.text_view_description)
        val buttonFavorite: MaterialButton = view.findViewById(R.id.button_favorite)
        val buttonEdit: Button = view.findViewById(R.id.button_edit)

        init {
            // Check if the userId is 123, then show buttonEdit, else hide it
            if (isLoggedIn) {
                buttonEdit.visibility = View.VISIBLE // Show the button
            } else {
                buttonEdit.visibility = View.GONE // Hide the button
            }
        }
    }

    /**
     * Get the `View` instance with information about a selected `DummyModel` from the list.
     *
     * @param position The position of the specified item.
     * @param convertView The current view holder.
     * @param parent The parent view which will group the view holder.
     *
     * @return A new view holder populated with the selected `DummyModel` data.
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        // The old view should be reused, if possible. You should check that this view is non-null
        // and of an appropriate type before using.
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)
        val viewHolder = (view.tag as? ViewHolder) ?: ViewHolder(view)

        populateView(view, getItem(position), position)

        // Set the new view holder and return the view object.
        view.tag = viewHolder
        return view
    }

    override fun populateView(v: View, model: Event, position: Int) {
        with(ViewHolder(v)) {
            // Fill out the Material Design card.
            //com.squareup.picasso.Picasso.get().load(dummy.url).into(imageViewPhoto)
            checkIfFavorite(model, buttonFavorite)

            textViewTitle.text = model.eventName
            textViewSubtitle.text = context.getString(
                R.string.secondary_text, model.eventType, CustomDate.getDateFromEpoch(model.eventDate)
            )
            textViewDescription.text = model.eventDescription
            // Set the button click listeners using method references.
            buttonFavorite.setOnClickListener {
                timeLineViewModel.addFavorite(model.id!!) { result ->
                    toggleFavorite(buttonFavorite, result)
                }
            }
        }
    }

    private fun checkIfFavorite(model: Event, buttonFavorite: MaterialButton) {
        model.id?.let { id ->
            timeLineViewModel.isFavorited(id) { result ->
                toggleFavorite(buttonFavorite, result)
            }
        }
    }

    private fun toggleFavorite(buttonFavorite: MaterialButton, value: Boolean) {
        if (value) { buttonFavorite.setIconResource(R.drawable.baseline_heart_icon_filled) }
        else { buttonFavorite.setIconResource(R.drawable.baseline_heart_icon) }
    }

}