package com.example.aprendecon.ui.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aprendecon.model.Modality
import com.example.aprendecon.model.Tutor
import androidx.compose.ui.tooling.preview.Preview
import com.example.aprendecon.ui.theme.AprendeConTheme

@Composable
fun TutorProfileScreen(
    tutor: Tutor,
    onBack: () -> Unit,
    onRequestTutoring: () -> Unit
) {
    val allWeekDays = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Transparent
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .navigationBarsPadding()
                ) {
                    Button(
                        onClick = onRequestTutoring,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5B21B6))
                    ) {
                        Text(text = "Solicitar tutoría", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = CircleShape,
                color = Color(0xFF5B21B6).copy(alpha = 0.1f),
                modifier = Modifier.size(80.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = tutor.name.take(2), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF5B21B6))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = tutor.name, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            Text(text = "${tutor.career} | ${tutor.semester}", fontSize = 13.sp, color = Color(0xFF64748B))

            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "⭐ ${tutor.rating}", fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                Text(text = " (${tutor.reviewsCount} reseñas)", fontSize = 14.sp, color = Color(0xFF64748B))
            }

            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(tutor.subjects) { subject ->
                    AssistChip(onClick = {}, label = { Text(subject) })
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Sobre mí", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = tutor.bio.ifEmpty { "Estudiante universitario disponible para dar asesorías académicas." },
                        fontSize = 14.sp,
                        color = Color(0xFF475569)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Días y horario disponible", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))
                    Spacer(modifier = Modifier.height(12.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(allWeekDays) { day ->
                            val isAvailable = tutor.availableDays.contains(day)
                            Surface(
                                shape = CircleShape,
                                color = if (isAvailable) Color(0xFF5B21B6) else Color(0xFFE2E8F0)
                            ) {
                                Text(
                                    text = day,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isAvailable) Color.White else Color(0xFF94A3B8)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "🕒 ${tutor.availableHours.ifEmpty { "09:00 a.m. - 06:00 p.m." }}", fontSize = 13.sp, color = Color(0xFF64748B))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = "📍 ${tutor.location.ifEmpty { "Biblioteca Universitaria, Edificio B, Sala 3" }}", fontSize = 13.sp, color = Color(0xFF64748B))
                }
            }
        }
    }
}

fun filterTimeSlotsByTutorRange(availableHoursStr: String): List<String> {
    val masterSlots = listOf(
        "07:00 a.m.", "08:00 a.m.", "09:00 a.m.", "10:00 a.m.", "11:00 a.m.",
        "12:00 p.m.", "01:00 p.m.", "02:00 p.m.", "03:00 p.m.", "04:00 p.m.",
        "05:00 p.m.", "06:00 p.m.", "07:00 p.m.", "08:00 p.m."
    )

    if (availableHoursStr.isBlank()) return masterSlots

    fun parseSlotHour(slotStr: String): Int {
        val clean = slotStr.lowercase().trim()
        val parts = clean.split(" ")
        val timePart = parts[0]
        val amPm = if (parts.size > 1) parts[1] else ""
        val hourMin = timePart.split(":")
        var h = hourMin[0].toIntOrNull() ?: 0
        if ((amPm.contains("p.m.") || amPm.contains("pm")) && h < 12) h += 12
        if ((amPm.contains("a.m.") || amPm.contains("am")) && h == 12) h = 0
        return h
    }

    val rangeParts = availableHoursStr.split("-")
    if (rangeParts.size == 2) {
        val startH = parseSlotHour(rangeParts[0])
        val endH = parseSlotHour(rangeParts[1])
        if (startH < endH) {
            val filtered = masterSlots.filter { slot ->
                val slotH = parseSlotHour(slot)
                slotH in startH..endH
            }
            if (filtered.isNotEmpty()) return filtered
        }
    }
    return masterSlots
}

@Composable
fun RequestSessionScreen(
    tutor: Tutor,
    bookedTimes: List<String>,
    onBack: () -> Unit,
    onConfirmRequest: (modality: Modality, subject: String, date: String, time: String) -> Unit
) {
    var selectedSubject by remember { mutableStateOf(tutor.subjects.firstOrNull() ?: "General") }
    var selectedDate by remember { mutableStateOf("15 de octubre de 2025") }

    val tutorSlots = remember(tutor.availableHours) {
        filterTimeSlotsByTutorRange(tutor.availableHours)
    }

    val availableTimeSlots = tutorSlots.filter { !bookedTimes.contains(it) }
    var selectedTime by remember { mutableStateOf(availableTimeSlots.firstOrNull() ?: tutorSlots.firstOrNull() ?: "09:00 a.m.") }

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Transparent
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Solicitar tutoría presencial", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF5B21B6).copy(alpha = 0.1f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = tutor.name.take(2), fontWeight = FontWeight.Bold, color = Color(0xFF5B21B6))
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = tutor.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = tutor.career, fontSize = 13.sp, color = Color(0xFF64748B))
                        Text(text = "⭐ ${tutor.rating}", fontSize = 12.sp, color = Color(0xFFD97706))
                    }
                }
            }

            Text(text = "Materia", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(tutor.subjects) { sub ->
                    FilterChip(
                        selected = selectedSubject == sub,
                        onClick = { selectedSubject = sub },
                        label = { Text(sub) }
                    )
                }
            }

            Text(text = "Lugar de reunión para clases presenciales", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE9FE))
            ) {
                Text(
                    text = "📍 ${tutor.location.ifEmpty { "Biblioteca Universitaria, Edificio B, Sala 3" }}",
                    modifier = Modifier.padding(12.dp),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF5B21B6)
                )
            }

            Text(
                text = "Horarios del tutor (${tutor.availableHours.ifEmpty { "09:00 a.m. - 06:00 p.m." }})",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF1E293B)
            )

            if (availableTimeSlots.isEmpty()) {
                Text(text = "No hay horarios libres para esta fecha en el rango del tutor.", color = Color(0xFFDC2626), fontSize = 13.sp)
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(tutorSlots) { time ->
                        val isBooked = bookedTimes.contains(time)
                        FilterChip(
                            selected = selectedTime == time && !isBooked,
                            enabled = !isBooked,
                            onClick = { if (!isBooked) selectedTime = time },
                            label = { Text(if (isBooked) "$time (Ocupado)" else time) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onConfirmRequest(Modality.PRESENTIAL, selectedSubject, selectedDate, selectedTime) },
                enabled = availableTimeSlots.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5B21B6))
            ) {
                Text(text = "Continuar y agendar", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ConfirmationScreen(
    tutorName: String,
    subject: String,
    modality: Modality,
    date: String,
    time: String,
    location: String = "Biblioteca Universitaria, Edificio B, Sala 3",
    onViewSessions: () -> Unit,
    onBackHome: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF10B981),
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¡Listo!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )
            Text(
                text = "Tu tutoría ha sido solicitada.",
                fontSize = 15.sp,
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DetailRow(label = "Tutor:", value = tutorName)
                    DetailRow(label = "Materia:", value = subject)
                    DetailRow(label = "Modalidad:", value = "Presencial")
                    DetailRow(label = "Fecha:", value = date)
                    DetailRow(label = "Hora:", value = time)
                    DetailRow(label = "Lugar:", value = location)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onViewSessions,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5B21B6))
            ) {
                Text(text = "Ver en Mis tutorías", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(onClick = onBackHome) {
                Text(text = "Volver al inicio", color = Color(0xFF5B21B6), fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color(0xFF64748B), fontSize = 14.sp)
        Text(text = value, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B), fontSize = 14.sp)
    }
}
