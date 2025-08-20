import SwiftUI
import shared

struct ContentView: View {
    @StateObject private var viewModel = DentalViewModel()
    @State private var selectedTab = 0
    
    var body: some View {
        TabView(selection: $selectedTab) {
            HomeView(viewModel: viewModel)
                .tabItem {
                    Image(systemName: "house.fill")
                    Text("Trang chủ")
                }
                .tag(0)
            
            AppointmentsView(appointments: viewModel.appointments)
                .tabItem {
                    Image(systemName: "calendar")
                    Text("Lịch khám")
                }
                .tag(1)
            
            PatientRecordsView(patient: viewModel.patient, treatments: viewModel.treatments)
                .tabItem {
                    Image(systemName: "person.fill")
                    Text("Hồ sơ")
                }
                .tag(2)
            
            DentalTipsView(tips: viewModel.dentalTips)
                .tabItem {
                    Image(systemName: "lightbulb.fill")
                    Text("Mẹo hay")
                }
                .tag(3)
        }
        .accentColor(.green)
    }
}

struct HomeView: View {
    let viewModel: DentalViewModel
    
    var body: some View {
        NavigationView {
            ScrollView {
                VStack(spacing: 20) {
                    // Welcome Card
                    VStack(spacing: 12) {
                        Text("🦷 Chào mừng đến với Pretty Teeth!")
                            .font(.title2)
                            .fontWeight(.medium)
                            .foregroundColor(.green)
                            .multilineTextAlignment(.center)
                        
                        Text("Ứng dụng chăm sóc răng miệng thông minh")
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                            .multilineTextAlignment(.center)
                    }
                    .padding()
                    .background(Color(.systemBackground))
                    .cornerRadius(12)
                    .shadow(color: .black.opacity(0.1), radius: 4, x: 0, y: 2)
                    
                    // Quick Actions
                    VStack(alignment: .leading, spacing: 12) {
                        Text("Dịch vụ chính")
                            .font(.headline)
                            .foregroundColor(.green)
                        
                        LazyVGrid(columns: [
                            GridItem(.flexible()),
                            GridItem(.flexible())
                        ], spacing: 12) {
                            QuickActionCard(title: "Lịch khám", icon: "calendar", color: .blue)
                            QuickActionCard(title: "Hồ sơ", icon: "person.fill", color: .purple)
                            QuickActionCard(title: "Mẹo hay", icon: "lightbulb.fill", color: .orange)
                            QuickActionCard(title: "Tư vấn", icon: "message.fill", color: .green)
                        }
                    }
                    
                    // Daily Tip
                    VStack(alignment: .leading, spacing: 8) {
                        HStack {
                            Image(systemName: "lightbulb.fill")
                                .foregroundColor(.green)
                            Text("Mẹo hôm nay")
                                .font(.headline)
                                .foregroundColor(.green)
                        }
                        
                        Text("Đánh răng ít nhất 2 phút mỗi lần, 2 lần mỗi ngày. Sử dụng kem đánh răng có fluoride để bảo vệ men răng tốt nhất.")
                            .font(.body)
                            .foregroundColor(.secondary)
                    }
                    .padding()
                    .background(Color.green.opacity(0.1))
                    .cornerRadius(12)
                }
                .padding()
            }
            .navigationTitle("Pretty Teeth")
        }
    }
}

struct QuickActionCard: View {
    let title: String
    let icon: String
    let color: Color
    
    var body: some View {
        VStack(spacing: 8) {
            Image(systemName: icon)
                .font(.largeTitle)
                .foregroundColor(.white)
            
            Text(title)
                .font(.subheadline)
                .fontWeight(.medium)
                .foregroundColor(.white)
        }
        .frame(height: 80)
        .frame(maxWidth: .infinity)
        .background(color)
        .cornerRadius(12)
    }
}

struct AppointmentsView: View {
    let appointments: [Appointment]
    
    var body: some View {
        NavigationView {
            List(appointments, id: \.id) { appointment in
                VStack(alignment: .leading, spacing: 4) {
                    Text(appointment.appointmentType)
                        .font(.headline)
                    Text("Ngày: \(appointment.dateTime)")
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                    Text("Bác sĩ: \(appointment.doctorName)")
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                    Text("Trạng thái: \(appointment.status.name)")
                        .font(.caption)
                        .foregroundColor(.green)
                }
                .padding(.vertical, 4)
            }
            .navigationTitle("🗓️ Lịch khám")
        }
    }
}

struct PatientRecordsView: View {
    let patient: Patient?
    let treatments: [Treatment]
    
    var body: some View {
        NavigationView {
            List {
                if let patient = patient {
                    Section("Thông tin cá nhân") {
                        VStack(alignment: .leading, spacing: 4) {
                            Text("Họ tên: \(patient.name)")
                            Text("Điện thoại: \(patient.phone)")
                            Text("Địa chỉ: \(patient.address)")
                        }
                        .padding(.vertical, 4)
                    }
                }
                
                Section("Lịch sử điều trị") {
                    ForEach(treatments, id: \.id) { treatment in
                        VStack(alignment: .leading, spacing: 4) {
                            Text(treatment.treatmentType)
                                .font(.headline)
                            Text("Ngày: \(treatment.date)")
                                .font(.subheadline)
                                .foregroundColor(.secondary)
                            Text("Trạng thái: \(treatment.status.name)")
                                .font(.caption)
                                .foregroundColor(.green)
                        }
                        .padding(.vertical, 2)
                    }
                }
            }
            .navigationTitle("📋 Hồ sơ răng")
        }
    }
}

struct DentalTipsView: View {
    let tips: [DentalTip]
    
    var body: some View {
        NavigationView {
            List(tips, id: \.id) { tip in
                VStack(alignment: .leading, spacing: 8) {
                    HStack {
                        Text(tip.emoji)
                            .font(.title2)
                        Text(tip.title)
                            .font(.headline)
                    }
                    
                    Text(tip.content)
                        .font(.body)
                        .foregroundColor(.secondary)
                }
                .padding(.vertical, 4)
            }
            .navigationTitle("💡 Mẹo chăm sóc")
        }
    }
}

#Preview {
    ContentView()
}
