package com.example.calendarapp.ui.compose

import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.calendarapp.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayDetailScreen(
    year: Int,
    month: Int,
    day: Int,
    viewModel: TaskViewModel,
    navController: NavController,
    isToday: Boolean = false
) {
    val tasksState by viewModel.taskState.collectAsState()
    val tasksForDate = tasksState.tasks.filter { task ->
        val taskCalendar = Calendar.getInstance().apply {
            timeInMillis = task.task_detail.date
        }
        taskCalendar.get(Calendar.YEAR) == year && taskCalendar.get(Calendar.MONTH) == month && taskCalendar.get(
            Calendar.DAY_OF_MONTH
        ) == day
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                val title = if (isToday) "Today's Task" else "Tasks for $day/${month + 1}/$year"
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }, navigationIcon = {
                if (isToday.not()) {
                    BackButton {
                        navController.popBackStack()
                    }
                }
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("addTask/$year/$month/$day")
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task")
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(if (isToday) 0.dp else 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (tasksForDate.isEmpty()) {
                Text(text = "No tasks", style = MaterialTheme.typography.bodyMedium)
            } else {
                tasksForDate.forEach { task ->
                    TaskItem(task = task.task_detail, onClick = {
                        navController.navigate("taskDetail/${task.task_id}")
                    })
                }
            }
        }
    }
}