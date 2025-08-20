package com.prettyteeth.shared.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Appointment(
    val id: String,
    val patientName: String,
    val doctorName: String,
    val appointmentType: String,
    val dateTime: String, // Using String for simplicity, can be LocalDateTime
    val status: AppointmentStatus,
    val notes: String = ""
)

@Serializable
enum class AppointmentStatus {
    SCHEDULED,
    CONFIRMED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

@Serializable
data class Patient(
    val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val dateOfBirth: String,
    val address: String,
    val medicalHistory: List<String> = emptyList()
)

@Serializable
data class Treatment(
    val id: String,
    val patientId: String,
    val treatmentType: String,
    val description: String,
    val date: String,
    val status: TreatmentStatus,
    val cost: Double,
    val notes: String = ""
)

@Serializable
enum class TreatmentStatus {
    PLANNED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

@Serializable
data class DentalTip(
    val id: String,
    val title: String,
    val content: String,
    val category: TipCategory,
    val emoji: String
)

@Serializable
enum class TipCategory {
    DAILY_CARE,
    PREVENTION,
    EMERGENCY,
    NUTRITION,
    GENERAL
}
