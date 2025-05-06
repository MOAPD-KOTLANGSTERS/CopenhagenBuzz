import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import android.text.style.StyleSpan
import android.graphics.Typeface

class DateDecorator(private val date: CalendarDay) : DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == date
    }

    override fun decorate(view: DayViewFacade) {
        // Add bold styling to the date
        view.addSpan(StyleSpan(Typeface.BOLD))
    }
}