package com.example.aprendecon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.aprendecon.model.Modality
import com.example.aprendecon.model.Tutor
import com.example.aprendecon.model.TutoringSession
import com.example.aprendecon.ui.auth.LoginScreen
import com.example.aprendecon.ui.auth.RegisterScreen
import com.example.aprendecon.ui.auth.SplashScreen
import com.example.aprendecon.ui.booking.ConfirmationScreen
import com.example.aprendecon.ui.booking.RequestSessionScreen
import com.example.aprendecon.ui.booking.TutorProfileScreen
import com.example.aprendecon.ui.main.MainScreenContainer
import com.example.aprendecon.ui.session.ChatScreen
import com.example.aprendecon.ui.theme.AprendeConTheme

sealed class Screen {
    object Splash : Screen()
    object Login : Screen()
    object Register : Screen()
    data class Main(val initialTab: Int = 0) : Screen()
    data class TutorProfile(val tutor: Tutor) : Screen()
    data class RequestSession(val tutor: Tutor) : Screen()
    data class Confirmation(
        val tutorName: String,
        val subject: String,
        val modality: Modality,
        val date: String,
        val time: String,
        val location: String
    ) : Screen()
    data class Chat(val session: TutoringSession) : Screen()
}

class MainActivity : ComponentActivity() {
    private val viewModel: PrendeConViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val helper = com.example.aprendecon.data.PrendeConDbHelper(this)
            helper.readableDatabase
        } catch (e: Exception) {
            e.printStackTrace()
        }

        setContent {
            AprendeConTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }

                    when (val screen = currentScreen) {
                        is Screen.Splash -> SplashScreen(
                            onGetStarted = { currentScreen = Screen.Login }
                        )
                        is Screen.Login -> LoginScreen(
                            onLogin = { email, pwd ->
                                val success = viewModel.login(email, pwd)
                                if (success) {
                                    currentScreen = Screen.Main(initialTab = 0)
                                }
                                success
                            },
                            onNavigateToRegister = { currentScreen = Screen.Register }
                        )
                        is Screen.Register -> RegisterScreen(
                            onRegister = { name, email, pwd, career, cuatrimestre ->
                                viewModel.register(name, email, pwd, career, cuatrimestre)
                            },
                            onBackToLogin = { currentScreen = Screen.Login }
                        )
                        is Screen.Main -> MainScreenContainer(
                            viewModel = viewModel,
                            initialTab = screen.initialTab,
                            onNavigateToTutorDetail = { tutor -> currentScreen = Screen.TutorProfile(tutor) },
                            onNavigateToOnlineSession = { session ->
                                viewModel.selectSession(session)
                                currentScreen = Screen.Chat(session)
                            },
                            onNavigateToPresentialSession = { session ->
                                viewModel.selectSession(session)
                                currentScreen = Screen.Chat(session)
                            },
                            onNavigateToChat = { session ->
                                viewModel.selectSession(session)
                                currentScreen = Screen.Chat(session)
                            },
                            onLogout = { currentScreen = Screen.Login }
                        )
                        is Screen.TutorProfile -> TutorProfileScreen(
                            tutor = screen.tutor,
                            onBack = { currentScreen = Screen.Main(0) },
                            onRequestTutoring = { currentScreen = Screen.RequestSession(screen.tutor) }
                        )
                        is Screen.RequestSession -> {
                            val bookedTimes = viewModel.getBookedTimes(screen.tutor.name, "15 de octubre de 2025")
                            RequestSessionScreen(
                                tutor = screen.tutor,
                                bookedTimes = bookedTimes,
                                onBack = { currentScreen = Screen.TutorProfile(screen.tutor) },
                                onConfirmRequest = { modality, subject, date, time ->
                                    viewModel.bookSession(screen.tutor, subject, modality, date, time)
                                    currentScreen = Screen.Confirmation(
                                        screen.tutor.name, subject, modality, date, time, screen.tutor.location
                                    )
                                }
                            )
                        }
                        is Screen.Confirmation -> ConfirmationScreen(
                            tutorName = screen.tutorName,
                            subject = screen.subject,
                            modality = screen.modality,
                            date = screen.date,
                            time = screen.time,
                            location = screen.location,
                            onViewSessions = { currentScreen = Screen.Main(2) },
                            onBackHome = { currentScreen = Screen.Main(0) }
                        )
                        is Screen.Chat -> {
                            val messages by viewModel.messages.collectAsState()
                            val user by viewModel.currentUser.collectAsState()
                            ChatScreen(
                                session = screen.session,
                                messages = messages,
                                currentUserName = user.name,
                                onSendMessage = { content ->
                                    viewModel.sendMessage(screen.session.id, user.name, content)
                                },
                                onStartVideoCall = {},
                                onBack = { currentScreen = Screen.Main(2) }
                            )
                        }
                    }
                }
            }
        }
    }
}
