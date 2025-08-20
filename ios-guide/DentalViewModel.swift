import SwiftUI
import shared

class DentalViewModel: ObservableObject {
    private let repository = DentalRepository()
    
    @Published var appointments: [Appointment] = []
    @Published var patient: Patient?
    @Published var treatments: [Treatment] = []
    @Published var dentalTips: [DentalTip] = []
    
    init() {
        loadData()
    }
    
    func loadData() {
        // Load appointments
        appointments = repository.getUpcomingAppointments()
        
        // Load patient info
        patient = repository.getPatientInfo()
        
        // Load treatments
        treatments = repository.getTreatmentHistory()
        
        // Load dental tips
        dentalTips = repository.getDentalTips()
    }
}
