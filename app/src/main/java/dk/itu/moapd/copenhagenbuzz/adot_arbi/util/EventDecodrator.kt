import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import android.text.style.ForegroundColorSpan
import android.graphics.Color

class EventDecorator(
    private val events: Map<CalendarDay, List<String>> // Map of CalendarDay to List of Event Names
) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return events.containsKey(day) // Check if the day has events
    }

    override fun decorate(view: DayViewFacade) {
        // Highlight the date with a color
        view.addSpan(ForegroundColorSpan(Color.GREEN))
        // You can also add other decorations like background color, etc.

    }
}