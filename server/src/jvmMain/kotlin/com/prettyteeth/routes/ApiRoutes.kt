package com.prettyteeth.routes

import com.prettyteeth.models.*
import com.prettyteeth.repository.DataRepository
import com.prettyteeth.shared.models.*
import com.prettyteeth.shared.repository.DentalRepository
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
// Removed kotlinx.datetime imports - not needed for basic API
import java.io.File
import java.util.*

fun Application.configureApiRoutes(repository: DataRepository) {
    val dentalRepository = DentalRepository()
    
    routing {
        
        // ========== DENTAL CARE ENDPOINTS ==========
        
        // Lấy danh sách cuộc hẹn sắp tới
        get("/api/appointments") {
            val appointments = dentalRepository.getUpcomingAppointments()
            call.respond(appointments)
        }
        
        // Tạo cuộc hẹn mới
        post("/api/appointments") {
            try {
                val appointment = call.receive<Appointment>()
                // TODO: Save to database
                call.respond(HttpStatusCode.Created, mapOf(
                    "success" to true,
                    "message" to "Đã tạo cuộc hẹn thành công",
                    "appointment" to appointment
                ))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf(
                    "success" to false,
                    "message" to "Lỗi: ${e.message}"
                ))
            }
        }
        
        // Lấy thông tin bệnh nhân
        get("/api/patients/{patientId}") {
            val patientId = call.parameters["patientId"]
            if (patientId != null) {
                val patient = dentalRepository.getPatientInfo()
                call.respond(patient)
            } else {
                call.respond(HttpStatusCode.BadRequest, mapOf(
                    "success" to false,
                    "message" to "Thiếu patient ID"
                ))
            }
        }
        
        // Lấy lịch sử điều trị
        get("/api/treatments/{patientId}") {
            val patientId = call.parameters["patientId"]
            if (patientId != null) {
                val treatments = dentalRepository.getTreatmentHistory()
                call.respond(treatments)
            } else {
                call.respond(HttpStatusCode.BadRequest, mapOf(
                    "success" to false,
                    "message" to "Thiếu patient ID"
                ))
            }
        }
        
        // Thêm điều trị mới
        post("/api/treatments") {
            try {
                val treatment = call.receive<Treatment>()
                // TODO: Save to database
                call.respond(HttpStatusCode.Created, mapOf(
                    "success" to true,
                    "message" to "Đã thêm điều trị thành công",
                    "treatment" to treatment
                ))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf(
                    "success" to false,
                    "message" to "Lỗi: ${e.message}"
                ))
            }
        }
        
        // Lấy mẹo chăm sóc răng
        get("/api/dental-tips") {
            val tips = dentalRepository.getDentalTips()
            call.respond(tips)
        }
        
        // Lấy mẹo theo category
        get("/api/dental-tips/{category}") {
            val category = call.parameters["category"]
            val tips = dentalRepository.getDentalTips()
            val filteredTips = if (category != null) {
                tips.filter { it.category.name.lowercase() == category.lowercase() }
            } else {
                tips
            }
            call.respond(filteredTips)
        }
        
        // ========== SCHEDULE ENDPOINTS ==========
        
        // Xem tất cả lịch trình
        get("/api/schedules") {
            val schedules = repository.getSchedules()
            call.respond(ApiResponse(
                action = "xem",
                type = "lịch trình", 
                detail = "Lấy danh sách ${schedules.size} lịch trình",
                data = schedules.joinToString("; ") { "${it.date} ${it.time}: ${it.title}" }
            ))
        }
        
        // Thêm lịch trình mới
        post("/api/schedules") {
            try {
                val request = call.receive<ScheduleRequest>()
                val schedule = repository.addSchedule(request)
                call.respond(HttpStatusCode.Created, ApiResponse(
                    action = "thêm",
                    type = "lịch trình",
                    detail = "Đã thêm lịch trình '${schedule.title}' vào ngày ${schedule.date} lúc ${schedule.time}",
                    data = "ID: ${schedule.id}"
                ))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ApiResponse(
                    action = "thêm",
                    type = "lịch trình",
                    detail = "Lỗi: ${e.message}",
                    success = false
                ))
            }
        }
        
        // Xem chi tiết lịch trình
        get("/api/schedules/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val schedule = repository.getScheduleById(id)
            if (schedule != null) {
                call.respond(ApiResponse(
                    action = "xem",
                    type = "lịch trình",
                    detail = "Chi tiết: ${schedule.title} - ${schedule.date} ${schedule.time}",
                    data = "${schedule.description} (Loại: ${schedule.type})"
                ))
            } else {
                call.respond(HttpStatusCode.NotFound, ApiResponse(
                    action = "xem",
                    type = "lịch trình",
                    detail = "Không tìm thấy lịch trình với ID: $id",
                    success = false
                ))
            }
        }
        
        // Chỉnh sửa lịch trình
        put("/api/schedules/{id}") {
            val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
            try {
                val request = call.receive<ScheduleRequest>()
                val updated = repository.updateSchedule(id, request)
                if (updated != null) {
                    call.respond(ApiResponse(
                        action = "sửa",
                        type = "lịch trình",
                        detail = "Đã cập nhật lịch trình '${updated.title}' - ${updated.date} ${updated.time}"
                    ))
                } else {
                    call.respond(HttpStatusCode.NotFound, ApiResponse(
                        action = "sửa",
                        type = "lịch trình",
                        detail = "Không tìm thấy lịch trình để cập nhật",
                        success = false
                    ))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ApiResponse(
                    action = "sửa",
                    type = "lịch trình",
                    detail = "Lỗi: ${e.message}",
                    success = false
                ))
            }
        }
        
        // Xóa lịch trình
        delete("/api/schedules/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val deleted = repository.deleteSchedule(id)
            if (deleted) {
                call.respond(ApiResponse(
                    action = "xóa",
                    type = "lịch trình",
                    detail = "Đã xóa lịch trình thành công"
                ))
            } else {
                call.respond(HttpStatusCode.NotFound, ApiResponse(
                    action = "xóa",
                    type = "lịch trình",
                    detail = "Không tìm thấy lịch trình để xóa",
                    success = false
                ))
            }
        }
        
        // ========== IMAGE ENDPOINTS ==========
        
        // Xem tất cả hình ảnh
        get("/api/images") {
            val images = repository.getImages()
            call.respond(ApiResponse(
                action = "xem",
                type = "hình ảnh",
                detail = "Có ${images.size} hình ảnh đã lưu",
                data = images.take(5).joinToString("; ") { "${it.date}: ${it.description}" }
            ))
        }
        
        // Xem hình ảnh theo ngày
        get("/api/images/date/{date}") {
            val date = call.parameters["date"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val images = repository.getImagesByDate(date)
            call.respond(ApiResponse(
                action = "xem",
                type = "hình ảnh",
                detail = "Ngày $date có ${images.size} hình ảnh",
                data = images.joinToString("; ") { "${it.category}: ${it.description}" }
            ))
        }
        
        // Upload hình ảnh
        post("/api/images/upload") {
            try {
                val multipartData = call.receiveMultipart()
                var date = ""
                var description = ""
                var category = "progress"
                var fileName = ""
                var originalName = ""
                
                multipartData.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            when (part.name) {
                                "date" -> date = part.value
                                "description" -> description = part.value
                                "category" -> category = part.value
                            }
                        }
                        is PartData.FileItem -> {
                            originalName = part.originalFileName ?: "unknown"
                            val fileBytes = part.streamProvider().readBytes()
                            fileName = "${UUID.randomUUID()}.${originalName.substringAfterLast('.')}"
                            
                            // Tạo thư mục upload nếu chưa có
                            val uploadDir = File("uploads")
                            if (!uploadDir.exists()) uploadDir.mkdirs()
                            
                            // Lưu file
                            File("uploads/$fileName").writeBytes(fileBytes)
                        }
                        else -> {
                            // Xử lý các loại PartData khác
                        }
                    }
                    part.dispose()
                }
                
                if (date.isNotEmpty() && fileName.isNotEmpty()) {
                    val imageRecord = repository.addImage(date, fileName, originalName, description, category)
                    call.respond(HttpStatusCode.Created, ApiResponse(
                        action = "thêm",
                        type = "hình ảnh",
                        detail = "Đã lưu hình ảnh '$description' cho ngày $date",
                        data = "File: $originalName (ID: ${imageRecord.id})"
                    ))
                } else {
                    call.respond(HttpStatusCode.BadRequest, ApiResponse(
                        action = "thêm",
                        type = "hình ảnh",
                        detail = "Thiếu thông tin ngày hoặc file hình ảnh",
                        success = false
                    ))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ApiResponse(
                    action = "thêm",
                    type = "hình ảnh",
                    detail = "Lỗi upload: ${e.message}",
                    success = false
                ))
            }
        }
        
        // Xóa hình ảnh
        delete("/api/images/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val image = repository.getImageById(id)
            if (image != null) {
                // Xóa file vật lý
                File("uploads/${image.filename}").delete()
                // Xóa record
                repository.deleteImage(id)
                call.respond(ApiResponse(
                    action = "xóa",
                    type = "hình ảnh",
                    detail = "Đã xóa hình ảnh '${image.description}'"
                ))
            } else {
                call.respond(HttpStatusCode.NotFound, ApiResponse(
                    action = "xóa",
                    type = "hình ảnh",
                    detail = "Không tìm thấy hình ảnh để xóa",
                    success = false
                ))
            }
        }
        
        // ========== REMINDER ENDPOINTS ==========
        
        // Xem tất cả nhắc nhở
        get("/api/reminders") {
            val reminders = repository.getReminders()
            call.respond(ApiResponse(
                action = "xem",
                type = "nhắc nhở",
                detail = "Có ${reminders.size} nhắc nhở đang hoạt động",
                data = reminders.take(3).joinToString("; ") { "${it.scheduledDateTime}: ${it.title}" }
            ))
        }
        
        // Thêm nhắc nhở mới
        post("/api/reminders") {
            try {
                val request = call.receive<ReminderRequest>()
                val reminder = repository.addReminder(request)
                call.respond(HttpStatusCode.Created, ApiResponse(
                    action = "thêm",
                    type = "nhắc nhở",
                    detail = "Đã tạo nhắc nhở '${reminder.title}' cho ${reminder.scheduledDateTime}",
                    data = "Nội dung: ${reminder.message}"
                ))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ApiResponse(
                    action = "thêm",
                    type = "nhắc nhở",
                    detail = "Lỗi: ${e.message}",
                    success = false
                ))
            }
        }
        
        // Chỉnh sửa nhắc nhở
        put("/api/reminders/{id}") {
            val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest)
            try {
                val request = call.receive<ReminderRequest>()
                val updated = repository.updateReminder(id, request)
                if (updated != null) {
                    call.respond(ApiResponse(
                        action = "sửa",
                        type = "nhắc nhở",
                        detail = "Đã cập nhật nhắc nhở '${updated.title}' - ${updated.scheduledDateTime}"
                    ))
                } else {
                    call.respond(HttpStatusCode.NotFound, ApiResponse(
                        action = "sửa",
                        type = "nhắc nhở",
                        detail = "Không tìm thấy nhắc nhở để cập nhật",
                        success = false
                    ))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, ApiResponse(
                    action = "sửa",
                    type = "nhắc nhở",
                    detail = "Lỗi: ${e.message}",
                    success = false
                ))
            }
        }
        
        // Xóa nhắc nhở
        delete("/api/reminders/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val deleted = repository.deleteReminder(id)
            if (deleted) {
                call.respond(ApiResponse(
                    action = "xóa",
                    type = "nhắc nhở",
                    detail = "Đã xóa nhắc nhở thành công"
                ))
            } else {
                call.respond(HttpStatusCode.NotFound, ApiResponse(
                    action = "xóa",
                    type = "nhắc nhở",
                    detail = "Không tìm thấy nhắc nhở để xóa",
                    success = false
                ))
            }
        }
    }
}
