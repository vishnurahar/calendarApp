package com.example.calendarapp.ui.compose

import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.calendarapp.utils.getDaysInMonth
import com.example.calendarapp.utils.getFirstDayOfWeek
import com.example.calendarapp.utils.getMonthName
import com.example.calendarapp.viewmodel.TaskViewModel

@Composable
fun CalendarView(
    initialYear: Int,
    initialMonth: Int,
    onDayClick: (Int, Int, Int) -> Unit,
    viewModel: TaskViewModel,
    navController: NavController
) {
    var selectedYear by remember { mutableIntStateOf(initialYear) }
    var selectedMonth by remember { mutableIntStateOf(initialMonth) }
    var showMonthYearPicker by remember { mutableStateOf(false) }

    val taskState by viewModel.taskState.collectAsState()
    val daysInMonth = getDaysInMonth(selectedYear, selectedMonth)
    val firstDayOfWeek = getFirstDayOfWeek(selectedYear, selectedMonth)
    val daysOfWeek = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")
    val calendar = Calendar.getInstance()

    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {
                if (selectedMonth == Calendar.JANUARY) {
                    selectedMonth = Calendar.DECEMBER
                    selectedYear -= 1
                } else {
                    selectedMonth -= 1
                }
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month")
            }

            Text(
                text = "${getMonthName(selectedMonth)} $selectedYear",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { showMonthYearPicker = true }
            )

            IconButton(onClick = {
                if (selectedMonth == Calendar.DECEMBER) {
                    selectedMonth = Calendar.JANUARY
                    selectedYear += 1
                } else {
                    selectedMonth += 1
                }
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next Month")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
        ) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day, modifier = Modifier.weight(1f), textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val totalGrids = daysInMonth.size + (firstDayOfWeek - 1)
        var dayCounter = 0
        for (i in 0 until totalGrids step 7) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
            ) {
                for (j in 0..6) {
                    if (i + j >= firstDayOfWeek - 1) {
                        if (dayCounter < daysInMonth.size) {
                            val day = daysInMonth[dayCounter]
                            val isToday =
                                selectedYear == currentYear && selectedMonth == currentMonth && day == currentDay
                            val backgroundColor =
                                if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                            val textColor =
                                if (isToday) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

                            val taskCount = taskState.tasks.count {
                                val taskCalendar = Calendar.getInstance().apply {
                                    timeInMillis = it.task_detail.date
                                }
                                taskCalendar.get(Calendar.YEAR) == selectedYear && taskCalendar.get(
                                    Calendar.MONTH
                                ) == selectedMonth && taskCalendar.get(Calendar.DAY_OF_MONTH) == day
                            }
                            Box(modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(4.dp)
                                .clickable {
                                    onDayClick(selectedYear, selectedMonth, day)
                                }
                                .background(
                                    backgroundColor, shape = MaterialTheme.shapes.small
                                ), contentAlignment = Alignment.TopCenter) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = day.toString(),
                                        color = textColor,
                                        textAlign = TextAlign.Center
                                    )
                                    if (taskCount > 0) {
                                        Text(
                                            text = "$taskCount tasks",
                                            color = textColor,
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.labelSmall,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                            dayCounter++
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))
        DayDetailScreen(
            year = currentYear,
            month = currentMonth,
            day = currentDay,
            viewModel = viewModel,
            navController = navController,
            isToday = true
        )
    }

    if (showMonthYearPicker) {
        MonthYearPickerDialog(
            initialYear = selectedYear,
            initialMonth = selectedMonth,
            onDismissRequest = { showMonthYearPicker = false },
            onMonthYearSelected = { year, month ->
                selectedYear = year
                selectedMonth = month
            }
        )
    }
}

@Composable
fun MonthYearPickerDialog(
    initialYear: Int,
    initialMonth: Int,
    onDismissRequest: () -> Unit,
    onMonthYearSelected: (Int, Int) -> Unit
) {
    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    var selectedYear by remember { mutableIntStateOf(initialYear) }
    var selectedMonth by remember { mutableIntStateOf(initialMonth) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Select Month and Year") },
        text = {
            Column {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                ) {
                    months.forEachIndexed { index, month ->
                        Text(
                            text = month.take(3),
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .clickable { selectedMonth = index },
                            color = if (selectedMonth == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { selectedYear -= 1 }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Decrease Year")
                    }
                    Text(text = selectedYear.toString(), style = MaterialTheme.typography.bodyLarge)
                    IconButton(onClick = { selectedYear += 1 }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Increase Year")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onMonthYearSelected(selectedYear, selectedMonth)
                onDismissRequest()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}