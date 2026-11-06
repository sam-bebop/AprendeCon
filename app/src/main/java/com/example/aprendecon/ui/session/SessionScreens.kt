package com.example.aprendecon.ui.session

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.aprendecon.model.Message
import com.example.aprendecon.model.Modality
import com.example.aprendecon.model.TutoringSession
import com.example.aprendecon.ui.theme.AprendeConTheme

@Composable
fun ChatScreen(
    session: TutoringSession,
    messages: List<Message>,
    currentUserName: String,
    onSendMessage: (String) -> Unit,
    onStartVideoCall: () -> Unit = {},
    onBack: () -> Unit
) {
    var textInput by remember { mutableStateOf("") }
    val otherPersonName = if (currentUserName == session.tutorName) session.studentName else session.tutorName

    Scaffold(
        topBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = otherPersonName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text(text = "Asesoría de ${session.subject}", fontSize = 12.sp, color = Color(0xFF64748B))
                        }
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 12.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeholder = { Text("Escribe un mensaje...") },
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp),
                        shape = RoundedCornerShape(24.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (textInput.isNotBlank()) {
                                onSendMessage(textInput)
                                textInput = ""
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFF5B21B6), CircleShape)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar", tint = Color.White)
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(messages) { msg ->
                val isMe = (msg.senderName == currentUserName) || (msg.senderName == "Tú")
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = if (isMe) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isMe) Color(0xFF5B21B6) else Color(0xFFE2E8F0),
                        modifier = Modifier.widthIn(max = 280.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = msg.content,
                                color = if (isMe) Color.White else Color(0xFF1E293B),
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${msg.senderName} • ${msg.timestamp}",
                                color = if (isMe) Color.White.copy(alpha = 0.7f) else Color(0xFF64748B),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "Chat Screen", showBackground = true)
@Composable
fun ChatScreenPreview() {
    AprendeConTheme {
        ChatScreen(
            session = TutoringSession(
                id = "101",
                tutorName = "Ana López",
                subject = "Programación",
                modality = Modality.PRESENTIAL,
                date = "15 de octubre de 2025",
                time = "10:00 a.m."
            ),
            messages = listOf(
                Message("1", "101", "Ana López", "¡Hola! ¿Listo para nuestra sesión de programación?", "09:00 a.m."),
                Message("2", "101", "Tú", "¡Hola Ana! Sí, nos vemos a las 10.", "09:05 a.m.")
            ),
            currentUserName = "Tú",
            onSendMessage = {},
            onStartVideoCall = {},
            onBack = {}
        )
    }
}
