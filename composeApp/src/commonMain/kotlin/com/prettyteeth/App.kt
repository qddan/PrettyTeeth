package com.prettyteeth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

import com.prettyteeth.shared.Greeting

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        var currentScreen by remember { mutableStateOf("home") }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA))
        ) {
            // Top App Bar
            TopAppBar(
                title = { 
                    Text(
                        "Pretty Teeth", 
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4CAF50)
                )
            )
            
            when (currentScreen) {
                "home" -> HomeScreen(onNavigate = { currentScreen = it })
                "appointments" -> AppointmentsScreen(onBack = { currentScreen = "home" })
                "records" -> RecordsScreen(onBack = { currentScreen = "home" })
                "tips" -> TipsScreen(onBack = { currentScreen = "home" })
            }
        }
    }
}

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Welcome Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🦷 Chào mừng đến với Pretty Teeth!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF2E7D32)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Ứng dụng chăm sóc răng miệng thông minh",
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    textAlign = TextAlign.Center
                )
            }
        }
        
        // Quick Actions Grid
        Text(
            text = "Dịch vụ chính",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp),
            color = Color(0xFF2E7D32)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                title = "Lịch khám",
                icon = Icons.Default.DateRange,
                color = Color(0xFF2196F3),
                modifier = Modifier.weight(1f),
                onClick = { onNavigate("appointments") }
            )
            QuickActionCard(
                title = "Hồ sơ",
                icon = Icons.Default.Person,
                color = Color(0xFF9C27B0),
                modifier = Modifier.weight(1f),
                onClick = { onNavigate("records") }
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard(
                title = "Mẹo hay",
                icon = Icons.Default.Info,
                color = Color(0xFFFF9800),
                modifier = Modifier.weight(1f),
                onClick = { onNavigate("tips") }
            )
            QuickActionCard(
                title = "Tư vấn",
                icon = Icons.Default.Phone,
                color = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f),
                onClick = { /* TODO: Add consultation */ }
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Daily Tip Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E8)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Mẹo hôm nay",
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2E7D32)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Đánh răng ít nhất 2 phút mỗi lần, 2 lần mỗi ngày. Sử dụng kem đánh răng có fluoride để bảo vệ men răng tốt nhất.",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen(onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Lịch khám", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF2196F3)
            )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("🗓️ Lịch khám sắp tới", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(16.dp))
            
            // Mock appointments
            repeat(3) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Khám định kỳ #${index + 1}", fontWeight = FontWeight.Medium)
                        Text("Ngày: ${25 + index}/12/2024", color = Color.Gray)
                        Text("Thời gian: ${9 + index}:00 AM", color = Color.Gray)
                        Text("Bác sĩ: Dr. Nguyễn Văn A", color = Color.Gray)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen(onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Hồ sơ răng", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF9C27B0)
            )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("📋 Hồ sơ điều trị", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Thông tin cá nhân", fontWeight = FontWeight.Medium, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Họ tên: Nguyễn Văn B")
                    Text("Tuổi: 28")
                    Text("Điện thoại: 0901234567")
                    Text("Địa chỉ: TP.HCM")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Lịch sử điều trị", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(12.dp))
            
            repeat(2) { index ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Tẩy trắng răng", fontWeight = FontWeight.Medium)
                        Text("Ngày: ${10 + index}/11/2024", fontSize = 12.sp, color = Color.Gray)
                        Text("Trạng thái: Hoàn thành", fontSize = 12.sp, color = Color.Green)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipsScreen(onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Mẹo chăm sóc", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFFF9800)
            )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("💡 Mẹo chăm sóc răng miệng", fontSize = 18.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(16.dp))
            
            val tips = listOf(
                "🦷 Đánh răng đúng cách" to "Đánh răng ít nhất 2 phút, chuyển động tròn nhẹ nhàng",
                "🧵 Sử dụng chỉ nha khoa" to "Dùng chỉ nha khoa hàng ngày để loại bỏ mảng bám giữa răng",
                "🚭 Tránh thuốc lá" to "Hút thuốc gây hại nghiêm trọng đến răng miệng và nướu",
                "🍎 Ăn uống lành mạnh" to "Hạn chế đường và acid, tăng cường thực phẩm giàu canxi",
                "💧 Uống đủ nước" to "Nước giúp rửa trôi vi khuẩn và duy trì độ ẩm cho miệng"
            )
            
            tips.forEach { (title, content) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(title, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(content, color = Color(0xFF666666), fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
