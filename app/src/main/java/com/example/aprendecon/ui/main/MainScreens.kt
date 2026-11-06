package com.example.aprendecon.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aprendecon.PrendeConViewModel
import com.example.aprendecon.model.Modality
import com.example.aprendecon.model.NotificationItem
import com.example.aprendecon.model.Subject
import com.example.aprendecon.model.Tutor
import com.example.aprendecon.model.TutoringSession
import com.example.aprendecon.model.User

@Composable
fun MainScreenContainer(
    viewModel: PrendeConViewModel,
    initialTab: Int = 0,
    onNavigateToTutorDetail: (Tutor) -> Unit,
    onNavigateToOnlineSession: (TutoringSession) -> Unit,
    onNavigateToPresentialSession: (TutoringSession) -> Unit,
    onNavigateToChat: (TutoringSession) -> Unit,
    onLogout: () -> Unit
) {
    var selectedTab by remember(initialTab) { mutableIntStateOf(initialTab) }
    val tutors by viewModel.tutors.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val sessions by viewModel.sessions.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val user by viewModel.currentUser.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color(0xFF5B21B6)
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                    label = { Text("Buscar") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.CalendarToday, contentDescription = "Tutorías") },
                    label = { Text("Tutorías") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> HomeScreen(
                    tutors = tutors,
                    subjects = subjects,
                    notifications = notifications,
                    onTutorClick = onNavigateToTutorDetail,
                    onSearchClick = { selectedTab = 1 }
                )
                1 -> SearchScreen(
                    subjects = subjects,
                    tutors = tutors,
                    onTutorClick = onNavigateToTutorDetail
                )
                2 -> MySessionsScreen(
                    sessions = sessions,
                    user = user,
                    onChatClick = onNavigateToChat,
                    onConfirmSession = { sessionId -> viewModel.confirmSession(sessionId) },
                    onCancelSession = { sessionId -> viewModel.cancelSession(sessionId) }
                )
                3 -> ProfileScreen(
                    user = user,
                    onUpdateProfile = { name, email, career, cuatrimestre, isTutor, rate, cuatrisTutor, materiasTutor, bio, days, hours, ubi ->
                        viewModel.updateProfile(name, email, career, cuatrimestre, isTutor, rate, cuatrisTutor, materiasTutor, bio, days, hours, ubi)
                    },
                    onLogout = onLogout
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    tutors: List<Tutor>,
    subjects: List<Subject>,
    notifications: List<NotificationItem>,
    onTutorClick: (Tutor) -> Unit,
    onSearchClick: () -> Unit
) {
    var showNotifDialog by remember { mutableStateOf(false) }

    if (showNotifDialog) {
        AlertDialog(
            onDismissRequest = { showNotifDialog = false },
            title = { Text("Notificaciones") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (notifications.isEmpty()) {
                        Text("No tienes notificaciones.")
                    } else {
                        notifications.forEach { notif ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(text = notif.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(text = notif.message, fontSize = 12.sp, color = Color(0xFF475569))
                                    Text(text = notif.timestamp, fontSize = 10.sp, color = Color(0xFF94A3B8))
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showNotifDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "AprendeCon",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5B21B6)
                    )
                    Text(
                        text = "Tu universidad, mejores resultados",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF5B21B6).copy(alpha = 0.1f),
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { showNotifDialog = true }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = Color(0xFF5B21B6))
                        if (notifications.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .align(Alignment.TopEnd)
                                    .offset(x = (-4).dp, y = 4.dp)
                                    .background(Color(0xFFDC2626), CircleShape)
                            )
                        }
                    }
                }
            }
        }

        item {
            OutlinedCard(
                onClick = onSearchClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF64748B))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Buscar materia o tutor...", color = Color(0xFF94A3B8))
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF5B21B6))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Aprende hoy,\nlogra mañana",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Encuentra el tutor ideal para ti.",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 13.sp
                        )
                    }
                    Text(text = "📚", fontSize = 48.sp)
                }
            }
        }

        item {
            Text(
                text = "Tutores recomendados",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )
        }

        items(tutors) { tutor ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTutorClick(tutor) },
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
                        color = Color(0xFFE2E8F0),
                        modifier = Modifier.size(56.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = tutor.name.take(2), fontWeight = FontWeight.Bold, color = Color(0xFF5B21B6))
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = tutor.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))
                        Text(text = "${tutor.career} (${tutor.semester})", fontSize = 13.sp, color = Color(0xFF64748B))
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            items(tutor.subjects) { sub ->
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFFEDE9FE)
                                ) {
                                    Text(
                                        text = sub.trim(),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF5B21B6)
                                    )
                                }
                            }
                        }
                    }
                    Text(
                        text = "$${tutor.hourlyRate}/h",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5B21B6),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    subjects: List<Subject>,
    tutors: List<Tutor>,
    onTutorClick: (Tutor) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var selectedCuatri by remember { mutableStateOf("Todos") }
    val cuatrimestres = listOf("Todos") + (1..12).map { "$it°" }

    val careersFilterList = listOf(
        "Todas las carreras",
        "Manufactura Avanzada",
        "Ingeniería Financiera",
        "Tecnologías de la Información",
        "Mecánica Automotriz",
        "Comercio Internacional"
    )
    var careerFilterExpanded by remember { mutableStateOf(false) }
    var selectedCareerFilter by remember { mutableStateOf(careersFilterList[0]) }

    val filteredTutors = tutors.filter { tutor ->
        val matchesQuery = tutor.name.contains(query, ignoreCase = true) ||
                tutor.subjects.any { sub -> sub.contains(query, ignoreCase = true) }
        val matchesCuatri = selectedCuatri == "Todos" || tutor.semester.contains(selectedCuatri)
        val matchesCareer = selectedCareerFilter == careersFilterList[0] || tutor.career.contains(selectedCareerFilter, ignoreCase = true)
        matchesQuery && matchesCuatri && matchesCareer
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp)
    ) {
        Text(text = "Buscar tutores", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Buscar materia o tutor...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Carrera Dropdown Filter
        ExposedDropdownMenuBox(
            expanded = careerFilterExpanded,
            onExpandedChange = { careerFilterExpanded = !careerFilterExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedCareerFilter,
                onValueChange = {},
                readOnly = true,
                label = { Text("Filtrar por carrera") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = careerFilterExpanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = careerFilterExpanded,
                onDismissRequest = { careerFilterExpanded = false }
            ) {
                careersFilterList.forEach { c ->
                    DropdownMenuItem(
                        text = { Text(c, fontSize = 13.sp) },
                        onClick = {
                            selectedCareerFilter = c
                            careerFilterExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Filtrar por cuatrimestre", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(cuatrimestres) { cuatri ->
                FilterChip(
                    selected = selectedCuatri == cuatri,
                    onClick = { selectedCuatri = cuatri },
                    label = { Text(cuatri) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Materias", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(subjects) { subject ->
                AssistChip(
                    onClick = { query = subject.name },
                    label = { Text(subject.name) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Resultados (${filteredTutors.size})", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(filteredTutors) { tutor ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onTutorClick(tutor) },
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
                            color = Color(0xFFE2E8F0),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = tutor.name.take(2), fontWeight = FontWeight.Bold, color = Color(0xFF5B21B6))
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = tutor.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(text = "${tutor.career} • ${tutor.semester}", fontSize = 12.sp, color = Color(0xFF64748B))
                            Spacer(modifier = Modifier.height(4.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                items(tutor.subjects) { sub ->
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFFEDE9FE)
                                    ) {
                                        Text(
                                            text = sub.trim(),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF5B21B6)
                                        )
                                    }
                                }
                            }
                        }
                        Text(text = "$${tutor.hourlyRate}/h", fontWeight = FontWeight.Bold, color = Color(0xFF5B21B6))
                    }
                }
            }
        }
    }
}

@Composable
fun MySessionsScreen(
    sessions: List<TutoringSession>,
    user: User,
    onChatClick: (TutoringSession) -> Unit,
    onConfirmSession: (String) -> Unit,
    onCancelSession: (String) -> Unit
) {
    var roleTab by remember { mutableIntStateOf(0) } // 0: Como Alumno, 1: Asesorías como Tutor
    var statusTab by remember { mutableIntStateOf(0) } // 0: Pendientes / Próximas, 1: Confirmadas/Historial

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp)
    ) {
        Text(text = "Mis tutorías y asesorías", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
        Spacer(modifier = Modifier.height(12.dp))

        if (user.isTutor) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { roleTab = 0 },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (roleTab == 0) Color(0xFF5B21B6) else Color(0xFFE2E8F0)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Como Alumno", color = if (roleTab == 0) Color.White else Color(0xFF1E293B))
                }
                Button(
                    onClick = { roleTab = 1 },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (roleTab == 1) Color(0xFF5B21B6) else Color(0xFFE2E8F0)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Asesorías como Tutor", color = if (roleTab == 1) Color.White else Color(0xFF1E293B))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { statusTab = 0 },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (statusTab == 0) Color(0xFF7C3AED) else Color(0xFFE2E8F0)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = if (roleTab == 1) "Pendientes / Citas" else "Próximas / Activas", color = if (statusTab == 0) Color.White else Color(0xFF1E293B))
            }
            Button(
                onClick = { statusTab = 1 },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (statusTab == 1) Color(0xFF7C3AED) else Color(0xFFE2E8F0)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Historial / Canceladas", color = if (statusTab == 1) Color.White else Color(0xFF1E293B))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val filteredSessions = sessions.filter { session ->
            val matchesRole = if (roleTab == 0) (session.studentId == user.id || session.studentName == user.name) else session.tutorName == user.name
            val matchesStatus = if (statusTab == 0) (session.status == "PENDIENTE" || session.status == "CONFIRMADA") else session.status == "CANCELADA"
            matchesRole && matchesStatus
        }

        if (filteredSessions.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No hay tutorías en esta sección", color = Color(0xFF64748B))
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(filteredSessions) { session ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = session.subject, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = when (session.status) {
                                        "CONFIRMADA" -> Color(0xFFDCFCE7)
                                        "PENDIENTE" -> Color(0xFFFEF3C7)
                                        else -> Color(0xFFFEE2E2)
                                    }
                                ) {
                                    Text(
                                        text = session.status,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = when (session.status) {
                                            "CONFIRMADA" -> Color(0xFF166534)
                                            "PENDIENTE" -> Color(0xFF92400E)
                                            else -> Color(0xFF991B1B)
                                        }
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (roleTab == 0) "Tutor: ${session.tutorName}" else "Alumno: ${session.studentName}",
                                fontSize = 14.sp,
                                color = Color(0xFF475569)
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF64748B))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "${session.date} - ${session.time}", fontSize = 13.sp, color = Color(0xFF64748B))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "📍 ${session.location}", fontSize = 12.sp, color = Color(0xFF64748B))

                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { onChatClick(session) },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("💬 Chat")
                                }
                                if (roleTab == 1 && session.status == "PENDIENTE") {
                                    Button(
                                        onClick = { onConfirmSession(session.id) },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Confirmar")
                                    }
                                }
                                if (session.status != "CANCELADA") {
                                    Button(
                                        onClick = { onCancelSession(session.id) },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Cancelar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: User,
    onUpdateProfile: (
        name: String, email: String, career: String, cuatrimestre: String,
        isTutor: Boolean, rate: Int, cuatrisTutor: String, materiasTutor: String,
        bio: String, days: String, hours: String, lugarReunion: String
    ) -> Unit,
    onLogout: () -> Unit
) {
    var isTutor by remember { mutableStateOf(user.isTutor) }
    var hourlyRate by remember { mutableStateOf(user.hourlyRate.toString()) }
    var cuatrisTutor by remember { mutableStateOf(user.cuatrimestresTutor) }
    var bio by remember { mutableStateOf(user.bio.ifEmpty { "Estudiante universitario disponible para dar asesorías." }) }
    var hoursRange by remember { mutableStateOf(user.availableHours.ifEmpty { "09:00 a.m. - 06:00 p.m." }) }
    var lugarReunion by remember { mutableStateOf(user.lugarReunion.ifEmpty { "Biblioteca Universitaria, Edificio B, Sala 3" }) }
    var savedMessage by remember { mutableStateOf(false) }

    val allDaysList = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
    var selectedDays by remember {
        mutableStateOf(
            if (user.availableDays.isNotEmpty()) user.availableDays.split(",").map { it.trim() }.toSet()
            else setOf("Lun", "Mar", "Mié", "Jue", "Vie")
        )
    }

    val cuatrisList = (1..12).map { "$it° cuatrimestre" }
    var cuatriExpanded by remember { mutableStateOf(false) }
    var selectedCuatri by remember { mutableStateOf(user.cuatrimestre.ifEmpty { cuatrisList[0] }) }

    val availableSubjectsList = listOf(
        "Programación", "Matemáticas", "Bases de Datos", "Física", "Química", "Inglés", "Administración", "Finanzas", "Manufactura Avanzada"
    )
    var selectedSubjectsSet by remember {
        mutableStateOf(
            if (user.materiasTutor.isNotEmpty()) user.materiasTutor.split(",").map { it.trim() }.toSet()
            else setOf("Programación", "Matemáticas")
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                shape = CircleShape,
                color = Color(0xFF5B21B6),
                modifier = Modifier.size(80.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = user.name.take(2).uppercase(), color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = user.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            Text(text = "Alumno de ${user.career}", fontSize = 13.sp, color = Color(0xFF64748B))

            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(text = "Información de la Cuenta (Lectura)", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1E293B))

                    OutlinedTextField(
                        value = user.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Nombre completo (No editable)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = user.email,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Correo institucional (No editable)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = user.career,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Carrera (No editable)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Cuatrimestre editable vía Dropdown
                    ExposedDropdownMenuBox(
                        expanded = cuatriExpanded,
                        onExpandedChange = { cuatriExpanded = !cuatriExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedCuatri,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Cuatrimestre actual") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = cuatriExpanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = cuatriExpanded,
                            onDismissRequest = { cuatriExpanded = false }
                        ) {
                            cuatrisList.forEach { q ->
                                DropdownMenuItem(
                                    text = { Text(q, fontSize = 13.sp) },
                                    onClick = {
                                        selectedCuatri = q
                                        cuatriExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Modo Tutor", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = "Ofrecer asesorías a otros alumnos", fontSize = 12.sp, color = Color(0xFF64748B))
                        }
                        Switch(
                            checked = isTutor,
                            onCheckedChange = { isTutor = it }
                        )
                    }

                    if (isTutor) {
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = bio,
                            onValueChange = { bio = it },
                            label = { Text("Sobre mí (Biografía)") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                        )

                        OutlinedTextField(
                            value = hourlyRate,
                            onValueChange = { hourlyRate = it },
                            label = { Text("Tarifa por hora ($)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(text = "Días de la semana disponibles", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(allDaysList) { day ->
                                val isSelected = selectedDays.contains(day)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        selectedDays = if (isSelected) selectedDays - day else selectedDays + day
                                    },
                                    label = { Text(day) },
                                    shape = CircleShape
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = hoursRange,
                            onValueChange = { hoursRange = it },
                            label = { Text("Rango de Horario (ej. 09:00 a.m. - 06:00 p.m.)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = lugarReunion,
                            onValueChange = { lugarReunion = it },
                            label = { Text("Lugar de reunión para clases presenciales") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = cuatrisTutor,
                            onValueChange = { cuatrisTutor = it },
                            label = { Text("Cuatrimestres que impartes (ej. 1, 3, 6)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(text = "Materias que impartes (Selección de chips)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(availableSubjectsList) { subject ->
                                val isSelected = selectedSubjectsSet.contains(subject)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        selectedSubjectsSet = if (isSelected) selectedSubjectsSet - subject else selectedSubjectsSet + subject
                                    },
                                    label = { Text(subject) }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            onUpdateProfile(
                                user.name,
                                user.email,
                                user.career,
                                selectedCuatri,
                                isTutor,
                                hourlyRate.toIntOrNull() ?: 120,
                                cuatrisTutor,
                                selectedSubjectsSet.joinToString(", "),
                                bio,
                                selectedDays.joinToString(","),
                                hoursRange,
                                lugarReunion
                            )
                            savedMessage = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5B21B6))
                    ) {
                        Text("Guardar cambios")
                    }

                    if (savedMessage) {
                        Text(
                            text = "¡Perfil actualizado en SQLite!",
                            color = Color(0xFF10B981),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ProfileMenuItem(icon = Icons.Default.ExitToApp, title = "Cerrar sesión", textColor = Color(0xFFDC2626), onClick = onLogout)
                }
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    textColor: Color = Color(0xFF1E293B),
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = textColor)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = textColor, modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF94A3B8))
    }
}
