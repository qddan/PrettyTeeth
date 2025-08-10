package com.prettyteeth.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val action: String,
    val type: String,
    val detail: String,
    val success: Boolean = true,
    val data: String? = null
)

@Serializable
data class Schedule(
    val id: String,
    val date: String,
    val time: String,
    val title: String,
    val description: String,
    val type: String = "appointment", // appointment, checkup, adjustment
    val completed: Boolean = false,
    val createdAt: String
)

@Serializable
data class ImageRecord(
    val id: String,
    val date: String,
    val filename: String,
    val originalName: String,
    val description: String,
    val category: String = "progress", // progress, before, after, xray
    val createdAt: String
)

@Serializable
data class Reminder(
    val id: String,
    val title: String,
    val message: String,
    val scheduledDateTime: String,
    val type: String = "general", // general, medication, appointment
    val active: Boolean = true,
    val createdAt: String
)

@Serializable
data class ScheduleRequest(
    val date: String,
    val time: String,
    val title: String,
    val description: String,
    val type: String = "appointment"
)

@Serializable
data class ReminderRequest(
    val title: String,
    val message: String,
    val scheduledDateTime: String,
    val type: String = "general"
)

@Serializable
data class ImageUploadResponse(
    val id: String,
    val filename: String,
    val message: String
)
