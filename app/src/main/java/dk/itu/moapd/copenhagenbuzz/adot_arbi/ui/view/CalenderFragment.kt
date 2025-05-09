package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.view

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentCalenderBinding
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel.CalenderViewModel
import dk.itu.moapd.copenhagenbuzz.adot_arbi.util.EventDecorator
import dk.itu.moapd.copenhagenbuzz.adot_arbi.util.ShowEventDetails
import java.time.LocalDate


/**
 * A fragment subclass of [BaseFragment] responsible for displaying a calendar view
 * with visual markers for scheduled events and showing event details by date selection.
 *
 * It uses a [MaterialCalendarView] with a custom [EventDecorator] to highlight days with events.
 */
class CalenderFragment : BaseFragment<FragmentCalenderBinding>(
    FragmentCalenderBinding::inflate,
    R.id.action_calenderFragment_to_timeLineFragment,
    R.id.action_calenderFragment_to_bookmarksFragment,
    R.id.action_calenderFragment_self,
    R.id.action_calenderFragment_to_mapsFragment,
    R.id.action_calenderFragment_to_addEventFragment,
) {

    /**
     * ViewModel used to observe and manage calendar-based event data.
     */
    private val calenderViewModel: CalenderViewModel by viewModels()

    /**
     * Called after the fragment’s view has been created.
     * Sets up event decorators on the calendar and displays a dialog with event details
     * when the user selects a specific date.
     *
     * @param view The created view hierarchy associated with the fragment.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendarView = view.findViewById<MaterialCalendarView>(R.id.calendarView)

        calenderViewModel.eventsByDate.observe(viewLifecycleOwner) { eventsMap ->
            val color = requireContext().getColor(com.google.android.material.R.color.material_dynamic_primary50)
            calendarView.addDecorator(EventDecorator(eventsMap, color))
        }

        calendarView.setOnDateChangedListener { widget, date, selected ->
            val eventsForSelectedDate = calenderViewModel.events.value
                ?.filter {
                    val eventDate = LocalDate.ofEpochDay(it.eventDate)
                    eventDate == LocalDate.of(date.year, date.month, date.day)
                } ?: emptyList()

            if (eventsForSelectedDate.isNotEmpty()) {
                val eventNames = eventsForSelectedDate.map { it.eventName }.toTypedArray()

                AlertDialog.Builder(requireContext())
                    .setTitle("Events on ${date.date}")
                    .setItems(eventNames) { _, which ->
                        val selectedEvent = eventsForSelectedDate[which]
                        val showEventDetails = ShowEventDetails(requireContext(), selectedEvent)
                        showEventDetails.show()
                    }
                    .setPositiveButton("Close", null)
                    .show()
            } else {
                Toast.makeText(requireContext(), "No events on this date", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
