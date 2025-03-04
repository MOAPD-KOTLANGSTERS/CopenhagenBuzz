package dk.itu.moapd.copenhagenbuzz.adot_arbi.model

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R

/**
 * A class to customize an adapter with a `ViewHolder` to populate a list of dummy objects in a
 * `ListView`.
 */
class CustomAdapter(private val context: Context, private var resource: Int,
                    data: List<DummyModel>) :
    ArrayAdapter<DummyModel>(context, R.layout.item_row, data) {

    /**
     * A set of private constants used in this class.
     */
    companion object {
        private val TAG = CustomAdapter::class.qualifiedName
    }

    /**
     * An internal view holder class used to represent the layout that shows a single `DummyModel`
     * instance in the `ListView`.
     */
    private class ViewHolder(view: View) {
        val imageViewPhoto: ImageView = view.findViewById(R.id.image_view_photo)
        val textViewTitle: TextView = view.findViewById(R.id.text_view_title)
        val textViewSubtitle: TextView = view.findViewById(R.id.text_view_subtitle)
        val textViewDescription: TextView = view.findViewById(R.id.text_view_description)
        val buttonThumbUp: Button = view.findViewById(R.id.button_thumb_up)
        val buttonFavorite: Button = view.findViewById(R.id.button_favorite)
        val buttonShare: Button = view.findViewById(R.id.button_share)
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

        // Populate the view holder with the selected `Dummy` data.
        Log.d(TAG, "Populate an item at position: $position")
        getItem(position)?.let { dummy ->
            populateViewHolder(viewHolder, dummy)
        }

        // Set the new view holder and return the view object.
        view.tag = viewHolder
        return view
    }

    /**
     * Populates the given [viewHolder] with data from the provided [dummy].
     *
     * @param viewHolder The ViewHolder to populate.
     * @param dummy The Dummy object containing the data to populate the ViewHolder with.
     */
    private fun populateViewHolder(viewHolder: ViewHolder, dummy: DummyModel) {
        with(viewHolder) {
            // Fill out the Material Design card.
            Picasso.get().load(dummy.url).into(imageViewPhoto)
            textViewTitle.text = dummy.cityName
            textViewSubtitle.text = context.getString(
                R.string.secondary_text, dummy.country, dummy.zipCode
            )
            textViewDescription.text = dummy.description

            // Set the button click listeners using method references.
            buttonThumbUp.setOnClickListener {
                showSnackBar("Thumb up: ${dummy.cityName}", it)
            }
            buttonFavorite.setOnClickListener {
                showSnackBar("Favorite: ${dummy.cityName}", it)
            }
            buttonShare.setOnClickListener {
                showSnackBar("Share: ${dummy.cityName}", it)
            }
        }
    }

    /**
     * Displays a SnackBar to show a brief message about the clicked button.
     *
     * The SnackBar is created using the clicked button and is shown at the bottom of the screen.
     *
     * @param message The message to be displayed in the SnackBar.
     * @param view The view to find a parent from.
     */
    private fun showSnackBar(message: String, view: View) {
        Snackbar.make(
            view, message, Snackbar.LENGTH_SHORT
        ).show()
    }

}