package com.prettyteeth.repository

import com.prettyteeth.models.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.concurrent.ConcurrentHashMap
import java.util.UUID

class DataRepository {
    private val schedules = ConcurrentHashMap<String, Schedule>()
    private val images = ConcurrentHashMap<String, ImageRecord>()
    private val reminders = ConcurrentHashMap<String, Reminder>()
    
    fun addSchedule(request: ScheduleRequest): Schedule {
        val id = UUID.randomUUID().toString()
        val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString()
        
        val schedule = Schedule(
            id = id,
            date = request.date,
            time = request.time,
            title = request.title,
            description = request.description,
            type = request.type,
            createdAt = currentTime
        )
        
        schedules[id] = schedule
        return schedule
    }
    
    fun getSchedules(): List<Schedule> {
        return schedules.values.sortedBy { it.date }
    }
    
    fun getScheduleById(id: String): Schedule? {
        return schedules[id]
    }
    
    fun updateSchedule(id: String, request: ScheduleRequest): Schedule? {
        val existing = schedules[id] ?: return null
        val updated = existing.copy(
            date = request.date,
            time = request.time,
            title = request.title,
            description = request.description,
            type = request.type
        )
        schedules[id] = updated
        return updated
    }
    
    fun deleteSchedule(id: String): Boolean {
        return schedules.remove(id) != null
    }
    
    fun addImage(date: String, filename: String, originalName: String, description: String, category: String): ImageRecord {
        val id = UUID.randomUUID().toString()
        val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString()
        
        val imageRecord = ImageRecord(
            id = id,
            date = date,
            filename = filename,
            originalName = originalName,
            description = description,
            category = category,
            createdAt = currentTime
        )
        
        images[id] = imageRecord
        return imageRecord
    }
    
    fun getImages(): List<ImageRecord> {
        return images.values.sortedByDescending { it.date }
    }
    
    fun getImagesByDate(date: String): List<ImageRecord> {
        return images.values.filter { it.date == date }.sortedBy { it.createdAt }
    }
    
    fun getImageById(id: String): ImageRecord? {
        return images[id]
    }
    
    fun deleteImage(id: String): Boolean {
        return images.remove(id) != null
    }
    
    fun addReminder(request: ReminderRequest): Reminder {
        val id = UUID.randomUUID().toString()
        val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString()
        
        val reminder = Reminder(
            id = id,
            title = request.title,
            message = request.message,
            scheduledDateTime = request.scheduledDateTime,
            type = request.type,
            createdAt = currentTime
        )
        
        reminders[id] = reminder
        return reminder
    }
    
    fun getReminders(): List<Reminder> {
        return reminders.values.filter { it.active }.sortedBy { it.scheduledDateTime }
    }
    
    fun getReminderById(id: String): Reminder? {
        return reminders[id]
    }
    
    fun updateReminder(id: String, request: ReminderRequest): Reminder? {
        val existing = reminders[id] ?: return null
        val updated = existing.copy(
            title = request.title,
            message = request.message,
            scheduledDateTime = request.scheduledDateTime,
            type = request.type
        )
        reminders[id] = updated
        return updated
    }
    
    fun deactivateReminder(id: String): Boolean {
        val existing = reminders[id] ?: return false
        reminders[id] = existing.copy(active = false)
        return true
    }
    
    fun deleteReminder(id: String): Boolean {
        return reminders.remove(id) != null
    }
}
