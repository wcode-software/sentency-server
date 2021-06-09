package org.wcode.utils

import java.util.*

object CalendarUtils {

    fun getMonthStartEnd(): Pair<Long, Long> {
        val start = getFirstDayMonth()
        val end = getLastDayMonth()
        return Pair(start, end)
    }

    private fun getFirstDayMonth(): Long {
        val start = Calendar.getInstance()
        start[Calendar.HOUR_OF_DAY] = 0
        start[Calendar.SECOND] = 0
        start[Calendar.MINUTE] = 0
        start[Calendar.MILLISECOND] = 0
        start[Calendar.DAY_OF_MONTH] = 1
        return start.timeInMillis
    }

    private fun getLastDayMonth(): Long {
        val end = Calendar.getInstance()
        end[Calendar.HOUR_OF_DAY] = 23
        end[Calendar.SECOND] = 59
        end[Calendar.MINUTE] = 59
        end[Calendar.MILLISECOND] = 0
        end[Calendar.DAY_OF_MONTH] = end.getActualMaximum(Calendar.DAY_OF_MONTH)
        return end.timeInMillis
    }
}
