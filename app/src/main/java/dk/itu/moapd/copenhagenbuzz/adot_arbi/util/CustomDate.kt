package dk.itu.moapd.copenhagenbuzz.adot_arbi.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

object CustomDate {
    private val delemiter = "/";
    fun of(day: Int, month: Int, year: Int) = "$day$delemiter$month$delemiter$year"
    fun getDateFromEpoch(epoch: Long) = LocalDate.ofEpochDay(epoch)

    // You can read some docu of date formats here https://www.baeldung.com/kotlin/dates
    fun getEpochFromString(input: String, dateFormat: String = "dd-MM-yyyy") : Long {
        if (input.isEmpty()) throw IllegalArgumentException("Please add a date")
        val lst = input.split(delemiter).map { it.toInt() }
        if (lst.size != 3) throw IllegalArgumentException("Date Must consist of dd${delemiter}MM${delemiter}yyyy")
        val date = LocalDate.of(lst[2], lst[1], lst[0])
        date.format(DateTimeFormatter.ofPattern(dateFormat))
        return date.toEpochDay()
    }
}