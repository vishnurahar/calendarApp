package com.example.calendarapp.networking

import com.example.calendarapp.model.CreateTaskModel
import com.example.calendarapp.model.CreateTaskResponse
import com.example.calendarapp.model.DeleteTaskRequest
import com.example.calendarapp.model.GetTaskResponse
import com.example.calendarapp.model.GetTasksRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CalendarApi {
    @POST("/api/getCalendarTaskList")
    suspend fun getTasks(
        @Body getTasksRequest: GetTasksRequest
    ): Response<GetTaskResponse>

    @POST("/api/storeCalendarTask")
    suspend fun createTask(
        @Body createTaskModel: CreateTaskModel
    ): Response<CreateTaskResponse>

    @POST("/api/deleteCalendarTask")
    suspend fun deleteTask(
        @Body deleteTaskRequest: DeleteTaskRequest
    ): Response<CreateTaskResponse>
}