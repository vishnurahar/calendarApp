package com.example.calendarapp.ui.compose

import android.icu.util.Calendar
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.calendarapp.viewmodel.TaskViewModel

@Composable
fun AppNavHost(navController: NavHostController, viewModel: TaskViewModel) {
    NavHost(navController = navController, startDestination = "calendar") {
        composable("calendar") {
            CalendarView(
                initialYear = Calendar.getInstance().get(Calendar.YEAR),
                initialMonth = Calendar.getInstance().get(Calendar.MONTH),
                onDayClick = { year, month, day ->
                    navController.navigate("dayDetail/${year}/${month}/$day")
                },
                viewModel = viewModel,
                navController = navController
            )
        }
        composable("dayDetail/{year}/{month}/{day}") { backStackEntry ->
            val year = backStackEntry.arguments?.getString("year")?.toInt() ?: 0
            val month = backStackEntry.arguments?.getString("month")?.toInt() ?: 0
            val day = backStackEntry.arguments?.getString("day")?.toInt() ?: 0

            DayDetailScreen(
                year = year,
                month = month,
                day = day,
                viewModel = viewModel,
                navController = navController
            )
        }
        composable("addTask/{year}/{month}/{day}") { backStackEntry ->
            val year = backStackEntry.arguments?.getString("year")?.toInt() ?: 0
            val month = backStackEntry.arguments?.getString("month")?.toInt() ?: 0
            val day = backStackEntry.arguments?.getString("day")?.toInt() ?: 0

            AddTaskScreen(
                year = year,
                month = month,
                day = day,
                viewModel = viewModel,
                navController = navController
            )
        }
        composable("taskDetail/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toInt() ?: 0

            TaskDetailScreen(
                taskId = taskId,
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}