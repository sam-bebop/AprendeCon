package com.example.aprendecon

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.aprendecon.data.PrendeConDbHelper
import com.example.aprendecon.model.Message
import com.example.aprendecon.model.Modality
import com.example.aprendecon.model.NotificationItem
import com.example.aprendecon.model.Subject
import com.example.aprendecon.model.Tutor
import com.example.aprendecon.model.TutoringSession
import com.example.aprendecon.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PrendeConViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = PrendeConDbHelper(application)

    private val _currentUser = MutableStateFlow(dbHelper.getUsuarioById("1"))
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    private val _tutors = MutableStateFlow(dbHelper.getAllTutors())
    val tutors: StateFlow<List<Tutor>> = _tutors.asStateFlow()

    private val _subjects = MutableStateFlow(
        listOf(
            Subject("1", "Matemáticas", "Ciencias Exactas"),
            Subject("2", "Programación", "Ingeniería"),
            Subject("3", "Física", "Ciencias Exactas"),
            Subject("4", "Química", "Ciencias Naturales"),
            Subject("5", "Inglés", "Idiomas"),
            Subject("6", "Administración", "Negocios")
        )
    )
    val subjects: StateFlow<List<Subject>> = _subjects.asStateFlow()

    private val _sessions = MutableStateFlow(dbHelper.getSessionsForUser(_currentUser.value.id, _currentUser.value.name))
    val sessions: StateFlow<List<TutoringSession>> = _sessions.asStateFlow()

    private val _notifications = MutableStateFlow(dbHelper.getNotificaciones(_currentUser.value.id, _currentUser.value.name))
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _selectedTutor = MutableStateFlow<Tutor?>(null)
    val selectedTutor: StateFlow<Tutor?> = _selectedTutor.asStateFlow()

    private val _selectedSession = MutableStateFlow<TutoringSession?>(null)
    val selectedSession: StateFlow<TutoringSession?> = _selectedSession.asStateFlow()

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun login(email: String, contrasena: String): Boolean {
        val user = dbHelper.validarLogin(email, contrasena)
        return if (user != null) {
            _currentUser.value = user
            _sessions.value = dbHelper.getSessionsForUser(user.id, user.name)
            _notifications.value = dbHelper.getNotificaciones(user.id, user.name)
            true
        } else {
            false
        }
    }

    fun register(name: String, email: String, contrasena: String, career: String, cuatrimestre: String): Boolean {
        val newUser = User(
            id = System.currentTimeMillis().toString(),
            name = name,
            email = email,
            password = contrasena,
            career = career,
            cuatrimestre = cuatrimestre
        )
        val success = dbHelper.registrarUsuario(newUser)
        if (success) {
            _currentUser.value = newUser
            _sessions.value = emptyList()
            _notifications.value = emptyList()
        }
        return success
    }

    fun getBookedTimes(tutorName: String, date: String): List<String> {
        return dbHelper.getBookedTimes(tutorName, date)
    }

    fun selectTutor(tutor: Tutor?) {
        _selectedTutor.value = tutor
    }

    fun selectSession(session: TutoringSession?) {
        _selectedSession.value = session
        if (session != null) {
            loadMessages(session.id)
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun bookSession(tutor: Tutor, subject: String, modality: Modality, date: String, time: String) {
        val user = _currentUser.value
        val newSession = TutoringSession(
            id = System.currentTimeMillis().toString(),
            tutorName = tutor.name,
            studentId = user.id,
            studentName = user.name,
            subject = subject,
            modality = Modality.PRESENTIAL,
            date = date,
            time = time,
            status = "PENDIENTE",
            location = tutor.location.ifEmpty { "Biblioteca Universitaria, Edificio B, Sala 3" }
        )
        dbHelper.insertSession(newSession)
        _sessions.value = dbHelper.getSessionsForUser(user.id, user.name)
        _notifications.value = dbHelper.getNotificaciones(user.id, user.name)
    }

    fun confirmSession(sessionId: String) {
        val user = _currentUser.value
        dbHelper.confirmarSesion(sessionId)
        _sessions.value = dbHelper.getSessionsForUser(user.id, user.name)
        _notifications.value = dbHelper.getNotificaciones(user.id, user.name)
    }

    fun cancelSession(sessionId: String) {
        val user = _currentUser.value
        dbHelper.cancelarSesion(sessionId)
        _sessions.value = dbHelper.getSessionsForUser(user.id, user.name)
        _notifications.value = dbHelper.getNotificaciones(user.id, user.name)
    }

    fun loadMessages(sessionId: String) {
        _messages.value = dbHelper.getMensajes(sessionId)
    }

    fun sendMessage(sessionId: String, senderName: String, content: String) {
        val user = _currentUser.value
        val newMsg = Message(
            id = System.currentTimeMillis().toString(),
            tutoringSessionId = sessionId,
            senderName = senderName,
            content = content,
            timestamp = "Hoy"
        )
        dbHelper.insertarMensaje(newMsg)
        loadMessages(sessionId)
        _notifications.value = dbHelper.getNotificaciones(user.id, user.name)
    }

    fun updateProfile(
        name: String,
        email: String,
        career: String,
        cuatrimestre: String,
        isTutor: Boolean,
        hourlyRate: Int,
        cuatrisTutor: String,
        materiasTutor: String,
        bio: String,
        availableDays: String,
        availableHours: String,
        lugarReunion: String
    ) {
        val current = _currentUser.value
        val updatedUser = current.copy(
            name = name,
            email = email,
            career = career,
            cuatrimestre = cuatrimestre,
            isTutor = isTutor,
            hourlyRate = hourlyRate,
            cuatrimestresTutor = cuatrisTutor,
            materiasTutor = materiasTutor,
            bio = bio,
            availableDays = availableDays,
            availableHours = availableHours,
            lugarReunion = lugarReunion
        )
        dbHelper.actualizarUsuario(updatedUser)
        _currentUser.value = dbHelper.getUsuarioById(updatedUser.id)
        _tutors.value = dbHelper.getAllTutors()
    }
}
