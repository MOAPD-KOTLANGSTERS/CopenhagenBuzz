package dk.itu.moapd.copenhagenbuzz.adot_arbi.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Utility object for handling date formatting and conversions between
 * string representations and epoch day values.
 */
object CustomDate {

    /**
     * The delimiter used in date strings (e.g., "dd/MM/yyyy").
     */
    private const val DELIMITER = "/"

    /**
     * Returns a date string formatted as "dd/MM/yyyy".
     *
     * @param day The day of the month.
     * @param month The month (1–12).
     * @param year The full year (e.g., 2025).
     * @return A formatted date string.
     */
    fun of(day: Int, month: Int, year: Int): String =
        "$day$DELIMITER$month$DELIMITER$year"

    /**
     * Converts an epoch day (number of days since 1970-01-01) to a formatted date string.
     *
     * @param epoch The epoch day value.
     * @return The formatted date string in "dd/MM/yyyy" format.
     */
    fun getDateFromEpoch(epoch: Long): String =
        LocalDate.ofEpochDay(epoch).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

    /**
     * Parses a date string (default "dd/MM/yyyy") and converts it to an epoch day.
     *
     * @param input The date string to parse.
     * @param dateFormat The expected format of the input string (default is "dd/MM/yyyy").
     * @return The epoch day equivalent of the parsed date.
     * @throws IllegalArgumentException If the input is empty or incorrectly formatted.
     */
    fun getEpochFromString(input: String, dateFormat: String = "dd/MM/yyyy"): Long {
        if (input.isEmpty()) throw IllegalArgumentException("Please add a date")
        val lst = input.split(DELIMITER).map { it.toInt() }
        if (lst.size != 3) throw IllegalArgumentException("Date must consist of dd${DELIMITER}MM${DELIMITER}yyyy")
        val date = LocalDate.of(lst[2], lst[1], lst[0])
        date.format(DateTimeFormatter.ofPattern(dateFormat)) // Ensures parsing succeeds, even if not used
        return date.toEpochDay()
    }
}
