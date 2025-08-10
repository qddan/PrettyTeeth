package com.prettyteeth

import com.prettyteeth.repository.DataRepository
import com.prettyteeth.routes.configureApiRoutes
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import java.io.File

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val repository = DataRepository()
    
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
    
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        anyHost()
    }
    
    configureApiRoutes(repository)
    
    routing {
        // Serve uploaded files
        get("/uploads/{filename}") {
            val filename = call.parameters["filename"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val file = File("uploads/$filename")
            if (file.exists()) {
                call.respondFile(file)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        
        // Main page
        get("/") {
            call.respondText(indexHtml, ContentType.Text.Html)
        }
    }
}

val indexHtml = """
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>PrettyTeeth - Trợ lý theo dõi niềng răng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body { 
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .container { margin-top: 2rem; }
        .card { 
            border-radius: 15px; 
            box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            backdrop-filter: blur(10px);
            background: rgba(255,255,255,0.95);
        }
        .btn-primary { 
            background: linear-gradient(45deg, #667eea, #764ba2);
            border: none;
            border-radius: 25px;
        }
        .response-card {
            margin-top: 1rem;
            border-left: 4px solid #667eea;
        }
        .logo { 
            font-size: 2rem; 
            font-weight: bold; 
            background: linear-gradient(45deg, #667eea, #764ba2);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="text-center mb-4">
            <div class="logo">
                <i class="fas fa-tooth"></i> PrettyTeeth
            </div>
            <p class="text-white">Trợ lý thông minh theo dõi quá trình niềng răng</p>
        </div>
        
        <div class="row">
            <!-- Quản lý lịch trình -->
            <div class="col-md-4">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h5><i class="fas fa-calendar-alt"></i> Quản lý lịch trình</h5>
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <label class="form-label">Ngày</label>
                            <input type="date" class="form-control" id="scheduleDate">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Giờ</label>
                            <input type="time" class="form-control" id="scheduleTime">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Tiêu đề</label>
                            <input type="text" class="form-control" id="scheduleTitle" placeholder="VD: Khám răng định kỳ">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Mô tả</label>
                            <textarea class="form-control" id="scheduleDesc" placeholder="Chi tiết cuộc hẹn..."></textarea>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Loại</label>
                            <select class="form-control" id="scheduleType">
                                <option value="appointment">Cuộc hẹn</option>
                                <option value="checkup">Kiểm tra</option>
                                <option value="adjustment">Điều chỉnh</option>
                            </select>
                        </div>
                        <button class="btn btn-primary w-100" onclick="addSchedule()">
                            <i class="fas fa-plus"></i> Thêm lịch trình
                        </button>
                        <button class="btn btn-outline-primary w-100 mt-2" onclick="viewSchedules()">
                            <i class="fas fa-list"></i> Xem lịch trình
                        </button>
                    </div>
                </div>
            </div>
            
            <!-- Quản lý hình ảnh -->
            <div class="col-md-4">
                <div class="card">
                    <div class="card-header bg-success text-white">
                        <h5><i class="fas fa-images"></i> Quản lý hình ảnh</h5>
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <label class="form-label">Ngày chụp</label>
                            <input type="date" class="form-control" id="imageDate">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Mô tả</label>
                            <input type="text" class="form-control" id="imageDesc" placeholder="VD: Hình ảnh sau 3 tháng niềng">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Loại ảnh</label>
                            <select class="form-control" id="imageCategory">
                                <option value="progress">Tiến trình</option>
                                <option value="before">Trước khi niềng</option>
                                <option value="after">Sau khi niềng</option>
                                <option value="xray">X-quang</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Chọn ảnh</label>
                            <input type="file" class="form-control" id="imageFile" accept="image/*">
                        </div>
                        <button class="btn btn-success w-100" onclick="uploadImage()">
                            <i class="fas fa-upload"></i> Upload ảnh
                        </button>
                        <button class="btn btn-outline-success w-100 mt-2" onclick="viewImages()">
                            <i class="fas fa-eye"></i> Xem ảnh
                        </button>
                    </div>
                </div>
            </div>
            
            <!-- Quản lý nhắc nhở -->
            <div class="col-md-4">
                <div class="card">
                    <div class="card-header bg-warning text-dark">
                        <h5><i class="fas fa-bell"></i> Quản lý nhắc nhở</h5>
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <label class="form-label">Tiêu đề</label>
                            <input type="text" class="form-control" id="reminderTitle" placeholder="VD: Uống thuốc">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Nội dung</label>
                            <textarea class="form-control" id="reminderMessage" placeholder="Chi tiết nhắc nhở..."></textarea>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Ngày giờ nhắc nhở</label>
                            <input type="datetime-local" class="form-control" id="reminderDateTime">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Loại</label>
                            <select class="form-control" id="reminderType">
                                <option value="general">Chung</option>
                                <option value="medication">Thuốc</option>
                                <option value="appointment">Cuộc hẹn</option>
                            </select>
                        </div>
                        <button class="btn btn-warning w-100" onclick="addReminder()">
                            <i class="fas fa-plus"></i> Tạo nhắc nhở
                        </button>
                        <button class="btn btn-outline-warning w-100 mt-2" onclick="viewReminders()">
                            <i class="fas fa-list"></i> Xem nhắc nhở
                        </button>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Response area -->
        <div class="row mt-4">
            <div class="col-12">
                <div class="card response-card" id="responseArea" style="display:none;">
                    <div class="card-body">
                        <h6>Phản hồi từ hệ thống:</h6>
                        <pre id="responseContent" class="mb-0"></pre>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function showResponse(data) {
            document.getElementById('responseContent').textContent = JSON.stringify(data, null, 2);
            document.getElementById('responseArea').style.display = 'block';
        }
        
        async function addSchedule() {
            const data = {
                date: document.getElementById('scheduleDate').value,
                time: document.getElementById('scheduleTime').value,
                title: document.getElementById('scheduleTitle').value,
                description: document.getElementById('scheduleDesc').value,
                type: document.getElementById('scheduleType').value
            };
            
            try {
                const response = await fetch('/api/schedules', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify(data)
                });
                const result = await response.json();
                showResponse(result);
            } catch (error) {
                showResponse({error: 'Lỗi kết nối: ' + error.message});
            }
        }
        
        async function viewSchedules() {
            try {
                const response = await fetch('/api/schedules');
                const result = await response.json();
                showResponse(result);
            } catch (error) {
                showResponse({error: 'Lỗi kết nối: ' + error.message});
            }
        }
        
        async function uploadImage() {
            const formData = new FormData();
            formData.append('date', document.getElementById('imageDate').value);
            formData.append('description', document.getElementById('imageDesc').value);
            formData.append('category', document.getElementById('imageCategory').value);
            formData.append('file', document.getElementById('imageFile').files[0]);
            
            try {
                const response = await fetch('/api/images/upload', {
                    method: 'POST',
                    body: formData
                });
                const result = await response.json();
                showResponse(result);
            } catch (error) {
                showResponse({error: 'Lỗi upload: ' + error.message});
            }
        }
        
        async function viewImages() {
            try {
                const response = await fetch('/api/images');
                const result = await response.json();
                showResponse(result);
            } catch (error) {
                showResponse({error: 'Lỗi kết nối: ' + error.message});
            }
        }
        
        async function addReminder() {
            const data = {
                title: document.getElementById('reminderTitle').value,
                message: document.getElementById('reminderMessage').value,
                scheduledDateTime: document.getElementById('reminderDateTime').value,
                type: document.getElementById('reminderType').value
            };
            
            try {
                const response = await fetch('/api/reminders', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify(data)
                });
                const result = await response.json();
                showResponse(result);
            } catch (error) {
                showResponse({error: 'Lỗi kết nối: ' + error.message});
            }
        }
        
        async function viewReminders() {
            try {
                const response = await fetch('/api/reminders');
                const result = await response.json();
                showResponse(result);
            } catch (error) {
                showResponse({error: 'Lỗi kết nối: ' + error.message});
            }
        }
    </script>
</body>
</html>
""".trimIndent()
