package dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.view

import EventDecorator
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import dk.itu.moapd.copenhagenbuzz.adot_arbi.R
import dk.itu.moapd.copenhagenbuzz.adot_arbi.databinding.FragmentCalenderBinding
import dk.itu.moapd.copenhagenbuzz.adot_arbi.ui.viewModel.CalenderViewModel
import dk.itu.moapd.copenhagenbuzz.adot_arbi.util.CustomDate.getDateFromEpoch
import dk.itu.moapd.copenhagenbuzz.adot_arbi.util.ShowEventDetails
import java.time.LocalDate

class CalenderFragment : BaseFragment<FragmentCalenderBinding>(
    FragmentCalenderBinding::inflate,
    R.id.action_calenderFragment_to_timeLineFragment,
    R.id.action_calenderFragment_to_bookmarksFragment,
    R.id.action_calenderFragment_self,
    R.id.action_calenderFragment_to_mapsFragment,
    R.id.action_calenderFragment_to_addEventFragment,
) {

    private val calenderViewModel: CalenderViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendarView = view.findViewById<MaterialCalendarView>(R.id.calendarView)

        calenderViewModel.events.observe(viewLifecycleOwner) { eventList ->
            val eventsMap = eventList.groupBy(
                {
                    val dateParts = getDateFromEpoch(it.eventDate).split("/")
                    CalendarDay.from(dateParts[2].toInt(), dateParts[1].toInt(), dateParts[0].toInt())
                },
                { it.eventName }
            )
            val color = requireContext().getColor(com.google.android.material.R.color.material_dynamic_primary50) // Replace with your color resource
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