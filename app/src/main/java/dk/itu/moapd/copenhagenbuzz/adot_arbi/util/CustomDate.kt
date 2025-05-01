package dk.itu.moapd.copenhagenbuzz.adot_arbi.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object CustomDate {
    private const val DELIMITER = "/"
    fun of(day: Int, month: Int, year: Int) : String = "$day$DELIMITER$month$DELIMITER$year"
    fun getDateFromEpoch(epoch: Long): LocalDate = LocalDate.ofEpochDay(epoch)

    // You can read some documentation of date formats here https://www.baeldung.com/kotlin/dates
    fun getEpochFromString(input: String, dateFormat: String = "dd-MM-yyyy") : Long {
        if (input.isEmpty()) throw IllegalArgumentException("Please add a date")
        val lst = input.split(DELIMITER).map { it.toInt() }
        if (lst.size != 3) throw IllegalArgumentException("Date Must consist of dd${DELIMITER}MM${DELIMITER}yyyy")
        val date = LocalDate.of(lst[2], lst[1], lst[0])
        date.format(DateTimeFormatter.ofPattern(dateFormat))
        return date.toEpochDay()
    }
}