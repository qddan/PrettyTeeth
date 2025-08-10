# PrettyTeeth 🦷

**A Smart Virtual Assistant for Orthodontic Treatment Tracking**

PrettyTeeth is a comprehensive web application designed to help users track their orthodontic (braces) treatment progress with intelligent features for schedule management, image storage, and timely reminders.

![PrettyTeeth Banner](https://via.placeholder.com/800x200/667eea/ffffff?text=PrettyTeeth+-+Smart+Braces+Tracking)

## ✨ Key Features

### 📅 **Schedule Management**
- **Add, view, edit, and delete** orthodontic appointments
- **Smart categorization**: Appointments, checkups, adjustments
- **Date and time tracking** with detailed descriptions
- **Progress monitoring** with completion status

### 📸 **Image Management by Date**
- **Upload and organize** dental progress photos by date
- **Category classification**: Progress, Before/After, X-rays
- **Secure file storage** with unique identifiers
- **Visual progress tracking** over time

### ⏰ **Smart Reminders**
- **Create personalized reminders** for medications and appointments
- **Flexible scheduling** with date-time precision
- **Reminder categories**: General, Medication, Appointments
- **Active/inactive status management**

### 🔗 **JSON-Based API**
- **RESTful API design** with consistent JSON responses
- **Structured response format**: `action`, `type`, `detail`, `success`, `data`
- **Vietnamese language support** for user-friendly interactions
- **Error handling** with detailed feedback

## 🎯 Why Choose PrettyTeeth?

### For Patients:
- **Centralized tracking** - All your orthodontic data in one place
- **Visual progress monitoring** - See your treatment journey through photos
- **Never miss appointments** - Smart reminders keep you on track
- **User-friendly interface** - Intuitive design for all ages
- **Mobile responsive** - Access from any device

### For Developers:
- **Modern tech stack** - Kotlin + Ktor framework
- **Clean architecture** - Separation of concerns with repository pattern
- **RESTful APIs** - Easy integration and extension
- **Type-safe models** - Kotlin data classes with serialization
- **Comprehensive error handling** - Robust API responses

### For Healthcare Integration:
- **JSON-based communication** - Easy integration with existing systems
- **Standardized data formats** - Consistent patient data structure
- **Scalable design** - Ready for database integration
- **Secure file handling** - Proper image upload and storage

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Gradle 8.0+
- Modern web browser

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/PrettyTeeth.git
   cd PrettyTeeth
   ```

2. **Run the application**
   ```bash
   ./gradlew run
   ```

3. **Access the application**
   - Open your browser to `http://localhost:8080`
   - Start tracking your orthodontic journey!

### Docker Support (Optional)
```bash
docker build -t prettyteeth .
docker run -p 8080:8080 prettyteeth
```

## 📖 Usage Guide

### Managing Schedules
```bash
# Add a new appointment
POST /api/schedules
{
  "date": "2025-01-15",
  "time": "14:30",
  "title": "Routine Checkup",
  "description": "Monthly orthodontic adjustment",
  "type": "checkup"
}

# Response
{
  "action": "thêm",
  "type": "lịch trình",
  "detail": "Đã thêm lịch trình 'Routine Checkup' vào ngày 2025-01-15 lúc 14:30",
  "success": true,
  "data": "ID: abc123-def456"
}
```

### Uploading Progress Images
```bash
# Upload dental progress photo
POST /api/images/upload
Content-Type: multipart/form-data

date: 2025-01-15
description: After 3 months of treatment
category: progress
file: [image-file]
```

### Setting Reminders
```bash
# Create medication reminder
POST /api/reminders
{
  "title": "Take Pain Medication",
  "message": "Remember to take your prescribed pain relief",
  "scheduledDateTime": "2025-01-15T20:00:00",
  "type": "medication"
}
```

## 💾 Data Storage Architecture

### Current Implementation: In-Memory Storage
PrettyTeeth currently uses **in-memory storage** with thread-safe concurrent data structures:

```kotlin
class DataRepository {
    private val schedules = ConcurrentHashMap<String, Schedule>()
    private val images = ConcurrentHashMap<String, ImageRecord>()
    private val reminders = ConcurrentHashMap<String, Reminder>()
}
```

**Benefits:**
- ⚡ **Ultra-fast performance** - No database overhead
- 🔧 **Easy development** - No setup required
- 📊 **Perfect for prototyping** - Quick iterations
- 🧪 **Ideal for testing** - Clean state per restart

**Limitations:**
- 📱 Data lost on application restart
- 📈 Limited scalability for production

### Migration to Persistent Storage

The architecture is designed for **easy migration** to persistent storage:

#### Database Integration Options:

**1. PostgreSQL Integration**
```kotlin
// Add to build.gradle.kts
implementation("org.postgresql:postgresql:42.7.0")
implementation("org.jetbrains.exposed:exposed-core:0.44.1")
implementation("org.jetbrains.exposed:exposed-dao:0.44.1")
implementation("org.jetbrains.exposed:exposed-jdbc:0.44.1")
```

**2. SQLite for Local Storage**
```kotlin
implementation("org.xerial:sqlite-jdbc:3.43.2.2")
```

**3. MongoDB for Document Storage**
```kotlin
implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.11.1")
```

#### File Storage Options:

**Current:** Local file system (`uploads/` directory)
```kotlin
val uploadDir = File("uploads")
if (!uploadDir.exists()) uploadDir.mkdirs()
File("uploads/$fileName").writeBytes(fileBytes)
```

**Upgrade Options:**
- ☁️ **AWS S3** - Cloud storage with CDN
- 🌐 **Google Cloud Storage** - Scalable object storage
- 🔒 **Azure Blob Storage** - Enterprise-grade storage

### Data Models

All data models are **serializable** and **type-safe**:

```kotlin
@Serializable
data class Schedule(
    val id: String,
    val date: String,
    val time: String,
    val title: String,
    val description: String,
    val type: String = "appointment",
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
    val category: String = "progress",
    val createdAt: String
)

@Serializable
data class Reminder(
    val id: String,
    val title: String,
    val message: String,
    val scheduledDateTime: String,
    val type: String = "general",
    val active: Boolean = true,
    val createdAt: String
)
```

## 🏗️ Architecture Overview

### Technology Stack
- **Backend**: Kotlin 1.9.20
- **Framework**: Ktor 2.3.7
- **Serialization**: Kotlinx Serialization
- **Concurrency**: Kotlin Coroutines
- **Frontend**: HTML5, CSS3, JavaScript (Bootstrap 5)
- **Build Tool**: Gradle 8.10

### Project Structure
```
src/
├── main/
│   ├── kotlin/
│   │   └── com/prettyteeth/
│   │       ├── Main.kt                 # Application entry point
│   │       ├── models/
│   │       │   └── Models.kt           # Data models
│   │       ├── repository/
│   │       │   └── DataRepository.kt   # Data access layer
│   │       └── routes/
│   │           └── ApiRoutes.kt        # REST API endpoints
│   └── resources/
│       └── logback.xml                 # Logging configuration
├── test/                               # Test files
└── uploads/                           # Image storage directory
```

### API Endpoints Summary

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/schedules` | GET | Get all schedules |
| `/api/schedules` | POST | Create new schedule |
| `/api/schedules/{id}` | GET | Get schedule by ID |
| `/api/schedules/{id}` | PUT | Update schedule |
| `/api/schedules/{id}` | DELETE | Delete schedule |
| `/api/images` | GET | Get all images |
| `/api/images/upload` | POST | Upload new image |
| `/api/images/date/{date}` | GET | Get images by date |
| `/api/images/{id}` | DELETE | Delete image |
| `/api/reminders` | GET | Get all reminders |
| `/api/reminders` | POST | Create new reminder |
| `/api/reminders/{id}` | PUT | Update reminder |
| `/api/reminders/{id}` | DELETE | Delete reminder |

## 🧪 Testing

### Manual Testing
1. Start the application: `./gradlew run`
2. Open `http://localhost:8080`
3. Test each feature through the web interface
4. Verify JSON responses in the browser console

### API Testing with curl
```bash
# Test schedule creation
curl -X POST http://localhost:8080/api/schedules \
  -H "Content-Type: application/json" \
  -d '{"date":"2025-01-15","time":"14:30","title":"Test Appointment","description":"Test description","type":"checkup"}'

# Test image upload
curl -X POST http://localhost:8080/api/images/upload \
  -F "date=2025-01-15" \
  -F "description=Test image" \
  -F "category=progress" \
  -F "file=@test-image.jpg"
```

## 🤝 Contributing

1. **Fork the repository**
2. **Create a feature branch**: `git checkout -b feature/new-feature`
3. **Commit changes**: `git commit -am 'Add new feature'`
4. **Push to branch**: `git push origin feature/new-feature`
5. **Submit a Pull Request**

### Development Guidelines
- Follow Kotlin coding conventions
- Add tests for new features
- Update documentation
- Ensure API consistency

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙋‍♀️ Support

For questions, issues, or suggestions:
- **Create an issue** on GitHub
- **Email**: support@prettyteeth.com
- **Documentation**: [Wiki](https://github.com/your-username/PrettyTeeth/wiki)

## 🔮 Roadmap

### Upcoming Features
- [ ] **Database integration** (PostgreSQL/MongoDB)
- [ ] **User authentication** and profiles
- [ ] **Data export/import** (PDF reports, CSV)
- [ ] **Push notifications** for reminders
- [ ] **Multi-language support**
- [ ] **Mobile app** (Android/iOS)
- [ ] **Healthcare provider portal**
- [ ] **Advanced analytics** and progress charts

### Version History
- **v1.0.0** - Initial release with core features
- **v0.9.0** - Beta release with API endpoints
- **v0.8.0** - Alpha release with basic functionality

---

**Made with ❤️ for better orthodontic care**

*PrettyTeeth - Because every smile deserves proper tracking* 🦷✨
