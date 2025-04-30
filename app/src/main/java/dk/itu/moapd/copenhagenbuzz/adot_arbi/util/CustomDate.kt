package dk.itu.moapd.copenhagenbuzz.adot_arbi.util

import java.time.LocalDate

object CustomDate {
    private val delemiter = "/";
    fun of(day: Int, month: Int, year: Int) = "$day$delemiter$month$delemiter$year"
    fun getDateFromEpoch(epoch: Long) = LocalDate.ofEpochDay(epoch)
    fun getEpochFromString(input: String) : Long {
        val lst = input.split(delemiter).map { it.toInt() }
        if (lst.size != 3) throw IllegalArgumentException()
        val date = LocalDate.of(lst[2], lst[1], lst[0])
        return date.toEpochDay()
    }
}