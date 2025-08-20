package com.prettyteeth.shared.repository

import com.prettyteeth.shared.models.*

class DentalRepository {
    
    fun getUpcomingAppointments(): List<Appointment> {
        return listOf(
            Appointment(
                id = "1",
                patientName = "Nguyễn Văn B",
                doctorName = "Dr. Nguyễn Văn A",
                appointmentType = "Khám định kỳ",
                dateTime = "25/12/2024 09:00",
                status = AppointmentStatus.CONFIRMED,
                notes = "Khám tổng quát và vệ sinh răng"
            ),
            Appointment(
                id = "2",
                patientName = "Nguyễn Văn B",
                doctorName = "Dr. Nguyễn Văn A",
                appointmentType = "Tẩy trắng răng",
                dateTime = "26/12/2024 10:00",
                status = AppointmentStatus.SCHEDULED,
                notes = "Điều trị tẩy trắng răng chuyên nghiệp"
            ),
            Appointment(
                id = "3",
                patientName = "Nguyễn Văn B",
                doctorName = "Dr. Nguyễn Văn A",
                appointmentType = "Nhổ răng khôn",
                dateTime = "27/12/2024 11:00",
                status = AppointmentStatus.SCHEDULED,
                notes = "Nhổ răng khôn hàm dưới"
            )
        )
    }
    
    fun getPatientInfo(): Patient {
        return Patient(
            id = "patient_1",
            name = "Nguyễn Văn B",
            phone = "0901234567",
            email = "nguyenvanb@email.com",
            dateOfBirth = "15/03/1996",
            address = "123 Đường ABC, Quận 1, TP.HCM",
            medicalHistory = listOf(
                "Dị ứng với penicillin",
                "Tiền sử niềng răng",
                "Không có bệnh mãn tính"
            )
        )
    }
    
    fun getTreatmentHistory(): List<Treatment> {
        return listOf(
            Treatment(
                id = "t1",
                patientId = "patient_1",
                treatmentType = "Tẩy trắng răng",
                description = "Tẩy trắng răng bằng công nghệ LED",
                date = "10/11/2024",
                status = TreatmentStatus.COMPLETED,
                cost = 2500000.0,
                notes = "Kết quả tốt, răng trắng sáng hơn 3 tông"
            ),
            Treatment(
                id = "t2",
                patientId = "patient_1",
                treatmentType = "Vệ sinh răng miệng",
                description = "Cạo vôi răng và đánh bóng",
                date = "11/11/2024",
                status = TreatmentStatus.COMPLETED,
                cost = 500000.0,
                notes = "Vệ sinh răng định kỳ, nướu khỏe mạnh"
            )
        )
    }
    
    fun getDentalTips(): List<DentalTip> {
        return listOf(
            DentalTip(
                id = "tip1",
                title = "Đánh răng đúng cách",
                content = "Đánh răng ít nhất 2 phút, chuyển động tròn nhẹ nhàng từ nướu xuống răng",
                category = TipCategory.DAILY_CARE,
                emoji = "🦷"
            ),
            DentalTip(
                id = "tip2", 
                title = "Sử dụng chỉ nha khoa",
                content = "Dùng chỉ nha khoa hàng ngày để loại bỏ mảng bám giữa răng",
                category = TipCategory.DAILY_CARE,
                emoji = "🧵"
            ),
            DentalTip(
                id = "tip3",
                title = "Tránh thuốc lá",
                content = "Hút thuốc gây hại nghiêm trọng đến răng miệng và nướu",
                category = TipCategory.PREVENTION,
                emoji = "🚭"
            ),
            DentalTip(
                id = "tip4",
                title = "Ăn uống lành mạnh",
                content = "Hạn chế đường và acid, tăng cường thực phẩm giàu canxi",
                category = TipCategory.NUTRITION,
                emoji = "🍎"
            ),
            DentalTip(
                id = "tip5",
                title = "Uống đủ nước",
                content = "Nước giúp rửa trôi vi khuẩn và duy trì độ ẩm cho miệng",
                category = TipCategory.GENERAL,
                emoji = "💧"
            )
        )
    }
}
