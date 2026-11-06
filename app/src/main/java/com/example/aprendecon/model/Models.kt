package com.example.aprendecon.model

data class User(
    val id: String = "1",
    val name: String = "Arturo Daniel Cruz Torres",
    val email: String = "arturo@upt.edu.mx",
    val password: String = "123456",
    val career: String = "Licenciatura en Ingeniería en Tecnologías de la Información e Innovación Digital",
    val cuatrimestre: String = "5° cuatrimestre",
    val isTutor: Boolean = false,
    val hourlyRate: Int = 120,
    val cuatrimestresTutor: String = "1, 3, 6",
    val materiasTutor: String = "Programación, Matemáticas",
    val bio: String = "Estudiante disponible para dar asesorías académicas.",
    val availableDays: String = "Lun,Mar,Mié,Jue,Vie",
    val availableHours: String = "09:00 a.m. - 06:00 p.m.",
    val lugarReunion: String = "Biblioteca Universitaria, Edificio B, Sala 3"
)

data class Tutor(
    val id: String,
    val name: String,
    val career: String,
    val semester: String, // Cuatrimestre
    val rating: Double,
    val reviewsCount: Int,
    val bio: String,
    val hourlyRate: Int,
    val subjects: List<String>,
    val availableDays: List<String>,
    val availableHours: String,
    val location: String = "Biblioteca Universitaria, Edificio B, Sala 3",
    val avatarUrl: String = ""
)

data class Subject(
    val id: String,
    val name: String,
    val category: String
)

enum class Modality {
    PRESENTIAL, ONLINE
}

data class TutoringSession(
    val id: String,
    val tutorName: String,
    val studentId: String = "1",
    val studentName: String = "Alumno",
    val subject: String,
    val modality: Modality = Modality.PRESENTIAL,
    val date: String,
    val time: String,
    val status: String = "PENDIENTE", // PENDIENTE, CONFIRMADA, CANCELADA
    val location: String = "Biblioteca Universitaria, Edificio B, Sala 3"
)

data class Message(
    val id: String,
    val tutoringSessionId: String,
    val senderName: String,
    val content: String,
    val timestamp: String
)

data class NotificationItem(
    val id: String,
    val userId: String = "1",
    val title: String,
    val message: String,
    val timestamp: String,
    val read: Boolean = false
)
