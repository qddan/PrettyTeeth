# PrettyTeeth API Documentation

## Overview

The PrettyTeeth API provides RESTful endpoints for managing orthodontic treatment data including schedules, images, and reminders. All responses follow a consistent JSON format with Vietnamese language support.

## Base URL
```
http://localhost:8080/api
```

## Response Format

All API responses follow this standard structure:

```json
{
  "action": "string",     // Action performed: "thêm", "xem", "sửa", "xóa"
  "type": "string",       // Data type: "lịch trình", "hình ảnh", "nhắc nhở"
  "detail": "string",     // Human-readable description in Vietnamese
  "success": boolean,     // Operation success status
  "data": "string"        // Optional additional data
}
```

## Authentication

Currently, no authentication is required. All endpoints are publicly accessible.

## Endpoints

### Schedules

#### Get All Schedules
```http
GET /api/schedules
```

**Response:**
```json
{
  "action": "xem",
  "type": "lịch trình",
  "detail": "Lấy danh sách 5 lịch trình",
  "success": true,
  "data": "2025-01-15 14:30: Routine Checkup; 2025-01-20 10:00: Adjustment"
}
```

#### Create Schedule
```http
POST /api/schedules
```

**Request Body:**
```json
{
  "date": "2025-01-15",
  "time": "14:30",
  "title": "Routine Checkup",
  "description": "Monthly orthodontic adjustment",
  "type": "checkup"
}
```

**Response:**
```json
{
  "action": "thêm",
  "type": "lịch trình",
  "detail": "Đã thêm lịch trình 'Routine Checkup' vào ngày 2025-01-15 lúc 14:30",
  "success": true,
  "data": "ID: abc123-def456"
}
```

#### Get Schedule by ID
```http
GET /api/schedules/{id}
```

**Response:**
```json
{
  "action": "xem",
  "type": "lịch trình",
  "detail": "Chi tiết: Routine Checkup - 2025-01-15 14:30",
  "success": true,
  "data": "Monthly orthodontic adjustment (Loại: checkup)"
}
```

#### Update Schedule
```http
PUT /api/schedules/{id}
```

**Request Body:**
```json
{
  "date": "2025-01-15",
  "time": "15:00",
  "title": "Updated Checkup",
  "description": "Updated description",
  "type": "checkup"
}
```

#### Delete Schedule
```http
DELETE /api/schedules/{id}
```

### Images

#### Get All Images
```http
GET /api/images
```

**Response:**
```json
{
  "action": "xem",
  "type": "hình ảnh",
  "detail": "Có 3 hình ảnh đã lưu",
  "success": true,
  "data": "2025-01-15: Progress after 3 months; 2025-01-10: Before treatment"
}
```

#### Upload Image
```http
POST /api/images/upload
```

**Request:** `multipart/form-data`
- `date`: Image date (YYYY-MM-DD)
- `description`: Image description
- `category`: Image category (progress, before, after, xray)
- `file`: Image file

**Response:**
```json
{
  "action": "thêm",
  "type": "hình ảnh",
  "detail": "Đã lưu hình ảnh 'Progress photo' cho ngày 2025-01-15",
  "success": true,
  "data": "File: photo.jpg (ID: xyz789-abc123)"
}
```

#### Get Images by Date
```http
GET /api/images/date/{date}
```

**Response:**
```json
{
  "action": "xem",
  "type": "hình ảnh",
  "detail": "Ngày 2025-01-15 có 2 hình ảnh",
  "success": true,
  "data": "progress: Before treatment; progress: After adjustment"
}
```

#### Delete Image
```http
DELETE /api/images/{id}
```

### Reminders

#### Get All Reminders
```http
GET /api/reminders
```

**Response:**
```json
{
  "action": "xem",
  "type": "nhắc nhở",
  "detail": "Có 4 nhắc nhở đang hoạt động",
  "success": true,
  "data": "2025-01-15T20:00: Take medication; 2025-01-16T09:00: Appointment reminder"
}
```

#### Create Reminder
```http
POST /api/reminders
```

**Request Body:**
```json
{
  "title": "Take Pain Medication",
  "message": "Remember to take your prescribed pain relief",
  "scheduledDateTime": "2025-01-15T20:00:00",
  "type": "medication"
}
```

**Response:**
```json
{
  "action": "thêm",
  "type": "nhắc nhở",
  "detail": "Đã tạo nhắc nhở 'Take Pain Medication' cho 2025-01-15T20:00:00",
  "success": true,
  "data": "Nội dung: Remember to take your prescribed pain relief"
}
```

#### Update Reminder
```http
PUT /api/reminders/{id}
```

**Request Body:**
```json
{
  "title": "Updated Reminder",
  "message": "Updated message",
  "scheduledDateTime": "2025-01-15T21:00:00",
  "type": "general"
}
```

#### Delete Reminder
```http
DELETE /api/reminders/{id}
```

## Data Types

### Schedule Types
- `appointment` - Cuộc hẹn
- `checkup` - Kiểm tra
- `adjustment` - Điều chỉnh

### Image Categories
- `progress` - Tiến trình
- `before` - Trước khi niềng
- `after` - Sau khi niềng
- `xray` - X-quang

### Reminder Types
- `general` - Chung
- `medication` - Thuốc
- `appointment` - Cuộc hẹn

## Error Handling

### Error Response Format
```json
{
  "action": "thêm",
  "type": "lịch trình",
  "detail": "Lỗi: Missing required field 'title'",
  "success": false
}
```

### HTTP Status Codes
- `200` - Success
- `201` - Created
- `400` - Bad Request
- `404` - Not Found
- `500` - Internal Server Error

## File Storage

### Image Upload
- Images are stored in the `uploads/` directory
- Unique filenames are generated using UUID
- Original filename is preserved in the database
- Supported formats: JPG, PNG, GIF, WebP

### File Access
```http
GET /uploads/{filename}
```

## Rate Limits

Currently, no rate limits are implemented. In production, consider implementing:
- Request per minute limits
- File upload size limits
- Concurrent request limits

## Examples

### cURL Examples

#### Create a Schedule
```bash
curl -X POST http://localhost:8080/api/schedules \
  -H "Content-Type: application/json" \
  -d '{
    "date": "2025-01-15",
    "time": "14:30",
    "title": "Routine Checkup",
    "description": "Monthly adjustment",
    "type": "checkup"
  }'
```

#### Upload an Image
```bash
curl -X POST http://localhost:8080/api/images/upload \
  -F "date=2025-01-15" \
  -F "description=Progress after 3 months" \
  -F "category=progress" \
  -F "file=@progress-photo.jpg"
```

#### Create a Reminder
```bash
curl -X POST http://localhost:8080/api/reminders \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Take Medication",
    "message": "Remember to take pain relief",
    "scheduledDateTime": "2025-01-15T20:00:00",
    "type": "medication"
  }'
```

### JavaScript Examples

#### Using Fetch API
```javascript
// Create schedule
async function createSchedule() {
  const response = await fetch('/api/schedules', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      date: '2025-01-15',
      time: '14:30',
      title: 'Routine Checkup',
      description: 'Monthly adjustment',
      type: 'checkup'
    })
  });
  
  const result = await response.json();
  console.log(result);
}

// Upload image
async function uploadImage(file, date, description) {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('date', date);
  formData.append('description', description);
  formData.append('category', 'progress');
  
  const response = await fetch('/api/images/upload', {
    method: 'POST',
    body: formData
  });
  
  const result = await response.json();
  console.log(result);
}
```

## Versioning

Current API version: `v1.0`

Future versions will be accessible via URL versioning:
- `http://localhost:8080/api/v1/schedules`
- `http://localhost:8080/api/v2/schedules`

## Support

For API support and questions:
- Create an issue on GitHub
- Email: api-support@prettyteeth.com
- Documentation updates: Submit a PR
