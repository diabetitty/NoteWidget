package it.local.notewidget

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateUtils {
    private val it = Locale.ITALIAN

    fun format(iso: String, style: Int): String {
        val d = LocalDate.parse(iso)
        val pattern = when (style) {
            0 -> "EEEE d MMMM"
            1 -> "EEE d MMM"
            2 -> "EEEE d MMM"
            3 -> "EEE d MMMM"
            4 -> "d MMMM yyyy"
            5 -> "d MMM yyyy"
            6 -> "dd/MM/yyyy"
            else -> "dd/MM"
        }
        return d.format(DateTimeFormatter.ofPattern(pattern, it))
            .replaceFirstChar { it.titlecase(it) }
    }

    fun today(): String = LocalDate.now().toString()
}
