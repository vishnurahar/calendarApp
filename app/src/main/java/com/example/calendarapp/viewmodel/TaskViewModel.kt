package com.example.calendarapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendarapp.model.TaskModel
import com.example.calendarapp.model.TaskState
import com.example.calendarapp.repository.CalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: CalendarRepository
) : ViewModel() {
    private val _taskState = MutableStateFlow(TaskState())
    val taskState = _taskState.asStateFlow()

    private val _errorState = MutableStateFlow("")
    val errorState = _errorState.asStateFlow()

    init {
        getTaskList()
    }

    private fun getTaskList() {
        viewModelScope.launch {
            try {
                val response = repository.getTasks()
                _taskState.value = TaskState(response.tasks)
            } catch (e: Exception) {
                _errorState.value = "Failed to fetch the tasks. Error ${e.message}"
            }
        }
    }

    fun addTask(task: TaskModel) {
        viewModelScope.launch {
            try {
                if (repository.createTask(task)) {
                    getTaskList()
                }
            } catch (e: Exception) {
                _errorState.value = "Failed to create the task. Error ${e.message}"
            }
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            try {
                if (repository.deleteTask(taskId)) {
                    _taskState.value = _taskState.value.copy(
                        tasks = _taskState.value.tasks.filterNot { it.task_id == taskId }
                    )
                    getTaskList()
                }
            } catch (e: Exception) {
                _errorState.value = "Failed to delete the task. Error ${e.message}"
            }
        }
    }
}