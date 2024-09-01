package com.example.calendarapp.utils

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import java.util.Locale

fun getDaysInMonth(
    year: Int,
    month: Int,
): List<Int> {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    val daysInMonth = mutableListOf<Int>()
    val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    for (day in 1..maxDay) {
        daysInMonth.add(day)
    }

    return daysInMonth
}

fun getFirstDayOfWeek(
    year: Int,
    month: Int,
): Int {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    return calendar.get(Calendar.DAY_OF_WEEK)
}

fun getMonthName(month: Int): String {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.MONTH, month)
    return SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.time)
}