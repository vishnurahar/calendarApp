package com.example.calendarapp.repository

import com.example.calendarapp.model.CreateTaskModel
import com.example.calendarapp.model.DeleteTaskRequest
import com.example.calendarapp.model.GetTaskResponse
import com.example.calendarapp.model.GetTasksRequest
import com.example.calendarapp.model.TaskModel
import com.example.calendarapp.networking.CalendarApi

interface CalendarRepository {
    suspend fun createTask(task: TaskModel) : Boolean
    suspend fun getTasks() : GetTaskResponse
    suspend fun deleteTask(taskId: Int) : Boolean
}

class CalendarRepositoryImpl(
    private val calendarApi: CalendarApi
) : CalendarRepository {
    override suspend fun createTask(task: TaskModel) : Boolean {
        val request = CreateTaskModel(task = task)
        return calendarApi.createTask(request).isSuccessful
    }

    override suspend fun getTasks() : GetTaskResponse {
        val getTaskRequest = GetTasksRequest()
        return calendarApi.getTasks(getTaskRequest).body()!!
    }

    override suspend fun deleteTask(taskId : Int) : Boolean {
        val request = DeleteTaskRequest(task_id = taskId)
        return calendarApi.deleteTask(request).isSuccessful
    }

}