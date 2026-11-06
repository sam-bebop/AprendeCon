package com.example.aprendecon.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Environment
import com.example.aprendecon.model.Message
import com.example.aprendecon.model.Modality
import com.example.aprendecon.model.NotificationItem
import com.example.aprendecon.model.Tutor
import com.example.aprendecon.model.TutoringSession
import com.example.aprendecon.model.User
import java.io.File

class PrendeConDbHelper(private val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "prendecon.db"
        private const val DATABASE_VERSION = 11

        // Tabla Tutores
        const val TABLA_TUTORES = "tutores"

        // 1. Tabla Usuarios
        const val TABLA_USUARIOS = "usuarios"
        const val COL_USUARIO_ID = "id"
        const val COL_USUARIO_NOMBRE = "nombre"
        const val COL_USUARIO_CORREO = "correo"
        const val COL_USUARIO_CONTRASENA = "contrasena"
        const val COL_USUARIO_CARRERA = "carrera"
        const val COL_USUARIO_CUATRIMESTRE = "cuatrimestre"
        const val COL_USUARIO_ES_TUTOR = "es_tutor"
        const val COL_USUARIO_TARIFA = "tarifa_hora"
        const val COL_USUARIO_CUATRIS_TUTOR = "cuatrimestres_tutor"
        const val COL_USUARIO_MATERIAS_TUTOR = "materias_tutor"
        const val COL_USUARIO_BIO_TUTOR = "bio_tutor"
        const val COL_USUARIO_DIAS_TUTOR = "dias_tutor"
        const val COL_USUARIO_HORARIO_TUTOR = "horario_tutor"
        const val COL_USUARIO_UBICACION_TUTOR = "ubicacion_tutor"

        // 2. Tabla Tutorías
        const val TABLA_TUTORIAS = "tutorias"
        const val COL_SESION_ID = "id"
        const val COL_SESION_TUTOR = "nombre_tutor"
        const val COL_SESION_ALUMNO_ID = "alumno_id"
        const val COL_SESION_ALUMNO_NOMBRE = "alumno_nombre"
        const val COL_SESION_MATERIA = "materia"
        const val COL_SESION_MODALIDAD = "modalidad"
        const val COL_SESION_FECHA = "fecha"
        const val COL_SESION_HORA = "hora"
        const val COL_SESION_ESTADO = "estado" // PENDIENTE, CONFIRMADA, CANCELADA
        const val COL_SESION_UBICACION = "ubicacion"

        // 3. Tabla Mensajes (Chat)
        const val TABLA_MENSAJES = "mensajes"
        const val COL_MENSAJE_ID = "id"
        const val COL_MENSAJE_SESION_ID = "tutoria_id"
        const val COL_MENSAJE_EMISOR = "emisor_nombre"
        const val COL_MENSAJE_CONTENIDO = "contenido"
        const val COL_MENSAJE_FECHA = "fecha_hora"

        // 4. Tabla Notificaciones
        const val TABLA_NOTIFICACIONES = "notificaciones"
        const val COL_NOTIF_ID = "id"
        const val COL_NOTIF_USUARIO_ID = "usuario_id"
        const val COL_NOTIF_TITULO = "titulo"
        const val COL_NOTIF_MENSAJE = "mensaje"
        const val COL_NOTIF_FECHA = "fecha_hora"
        const val COL_NOTIF_LEIDA = "leida"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLA_TUTORES (
                id TEXT PRIMARY KEY,
                nombre TEXT,
                carrera TEXT,
                semestre TEXT,
                calificacion REAL,
                numero_resenas INTEGER,
                biografia TEXT,
                tarifa_hora INTEGER,
                materias TEXT,
                dias_disponibles TEXT,
                horario TEXT,
                ubicacion TEXT
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLA_USUARIOS (
                $COL_USUARIO_ID TEXT PRIMARY KEY,
                $COL_USUARIO_NOMBRE TEXT,
                $COL_USUARIO_CORREO TEXT,
                $COL_USUARIO_CONTRASENA TEXT,
                $COL_USUARIO_CARRERA TEXT,
                $COL_USUARIO_CUATRIMESTRE TEXT,
                $COL_USUARIO_ES_TUTOR INTEGER,
                $COL_USUARIO_TARIFA INTEGER,
                $COL_USUARIO_CUATRIS_TUTOR TEXT,
                $COL_USUARIO_MATERIAS_TUTOR TEXT,
                $COL_USUARIO_BIO_TUTOR TEXT,
                $COL_USUARIO_DIAS_TUTOR TEXT,
                $COL_USUARIO_HORARIO_TUTOR TEXT,
                $COL_USUARIO_UBICACION_TUTOR TEXT
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLA_TUTORIAS (
                $COL_SESION_ID TEXT PRIMARY KEY,
                $COL_SESION_TUTOR TEXT,
                $COL_SESION_ALUMNO_ID TEXT,
                $COL_SESION_ALUMNO_NOMBRE TEXT,
                $COL_SESION_MATERIA TEXT,
                $COL_SESION_MODALIDAD TEXT,
                $COL_SESION_FECHA TEXT,
                $COL_SESION_HORA TEXT,
                $COL_SESION_ESTADO TEXT,
                $COL_SESION_UBICACION TEXT
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLA_MENSAJES (
                $COL_MENSAJE_ID TEXT PRIMARY KEY,
                $COL_MENSAJE_SESION_ID TEXT,
                $COL_MENSAJE_EMISOR TEXT,
                $COL_MENSAJE_CONTENIDO TEXT,
                $COL_MENSAJE_FECHA TEXT
            )
        """)

        db.execSQL("""
            CREATE TABLE $TABLA_NOTIFICACIONES (
                $COL_NOTIF_ID TEXT PRIMARY KEY,
                $COL_NOTIF_USUARIO_ID TEXT,
                $COL_NOTIF_TITULO TEXT,
                $COL_NOTIF_MENSAJE TEXT,
                $COL_NOTIF_FECHA TEXT,
                $COL_NOTIF_LEIDA INTEGER
            )
        """)

        insertarDatosIniciales(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLA_TUTORES")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_USUARIOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_TUTORIAS")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_MENSAJES")
        db.execSQL("DROP TABLE IF EXISTS $TABLA_NOTIFICACIONES")
        onCreate(db)
    }

    private fun insertarDatosIniciales(db: SQLiteDatabase) {
        val userCv = ContentValues().apply {
            put(COL_USUARIO_ID, "1")
            put(COL_USUARIO_NOMBRE, "Arturo Daniel Cruz Torres")
            put(COL_USUARIO_CORREO, "arturo@upt.edu.mx")
            put(COL_USUARIO_CONTRASENA, "123456")
            put(COL_USUARIO_CARRERA, "Licenciatura en Ingeniería en Tecnologías de la Información e Innovación Digital")
            put(COL_USUARIO_CUATRIMESTRE, "5° cuatrimestre")
            put(COL_USUARIO_ES_TUTOR, 0)
            put(COL_USUARIO_TARIFA, 120)
            put(COL_USUARIO_CUATRIS_TUTOR, "1, 3, 6")
            put(COL_USUARIO_MATERIAS_TUTOR, "Programación, Matemáticas")
            put(COL_USUARIO_BIO_TUTOR, "Estudiante disponible para asesorías académicas.")
            put(COL_USUARIO_DIAS_TUTOR, "Lun,Mar,Mié,Jue,Vie")
            put(COL_USUARIO_HORARIO_TUTOR, "09:00 a.m. - 06:00 p.m.")
            put(COL_USUARIO_UBICACION_TUTOR, "Biblioteca Universitaria, Edificio B, Sala 3")
        }
        db.insert(TABLA_USUARIOS, null, userCv)

        // Tutores iniciales por defecto
        val t1 = ContentValues().apply {
            put("id", "101_tutor")
            put("nombre", "Ana López")
            put("carrera", "Licenciatura en Ingeniería en Tecnologías de la Información e Innovación Digital")
            put("semestre", "4° cuatrimestre")
            put("calificacion", 4.8)
            put("numero_resenas", 24)
            put("biografia", "Estudiante de Ingeniería en Sistemas. Me gusta enseñar y compartir lo que sé. ¡Juntos podemos lograrlo!")
            put("tarifa_hora", 120)
            put("materias", "Programación,Matemáticas,Bases de datos")
            put("dias_disponibles", "Lun,Mar,Mié,Jue,Vie")
            put("horario", "08:00 a.m. - 02:00 p.m.")
            put("ubicacion", "Biblioteca Universitaria, Edificio B, Sala 3")
        }
        db.insert(TABLA_TUTORES, null, t1)

        val t2 = ContentValues().apply {
            put("id", "102_tutor")
            put("nombre", "Carlos Ruiz")
            put("carrera", "Licenciatura en Ingeniería Mecánica Automotriz")
            put("semestre", "6° cuatrimestre")
            put("calificacion", 4.7)
            put("numero_resenas", 18)
            put("biografia", "Apasionado por la física y las matemáticas avanzadas. Explicaciones claras y prácticas.")
            put("tarifa_hora", 100)
            put("materias", "Física,Matemáticas,Cálculo")
            put("dias_disponibles", "Mar,Jue,Sáb")
            put("horario", "10:00 a.m. - 03:00 p.m.")
            put("ubicacion", "Laboratorio Mecánico, Edificio C, Cubículo 2")
        }
        db.insert(TABLA_TUTORES, null, t2)

        val t3 = ContentValues().apply {
            put("id", "103_tutor")
            put("nombre", "Sofía Martínez")
            put("carrera", "Licenciatura en Comercio Internacional y Aduanas")
            put("semestre", "8° cuatrimestre")
            put("calificacion", 4.9)
            put("numero_resenas", 30)
            put("biografia", "Certificada en enseñanza de inglés y redacción académica. ¡Domina los idiomas conmigo!")
            put("tarifa_hora", 110)
            put("materias", "Inglés,Administración")
            put("dias_disponibles", "Lun,Mié,Vie")
            put("horario", "09:00 a.m. - 06:00 p.m.")
            put("ubicacion", "Edificio A, Sala de Estudio 1")
        }
        db.insert(TABLA_TUTORES, null, t3)
    }

    fun registrarUsuario(user: User): Boolean {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COL_USUARIO_ID, user.id)
            put(COL_USUARIO_NOMBRE, user.name)
            put(COL_USUARIO_CORREO, user.email)
            put(COL_USUARIO_CONTRASENA, user.password)
            put(COL_USUARIO_CARRERA, user.career)
            put(COL_USUARIO_CUATRIMESTRE, user.cuatrimestre)
            put(COL_USUARIO_ES_TUTOR, if (user.isTutor) 1 else 0)
            put(COL_USUARIO_TARIFA, user.hourlyRate)
            put(COL_USUARIO_CUATRIS_TUTOR, user.cuatrimestresTutor)
            put(COL_USUARIO_MATERIAS_TUTOR, user.materiasTutor)
            put(COL_USUARIO_BIO_TUTOR, user.bio)
            put(COL_USUARIO_DIAS_TUTOR, user.availableDays)
            put(COL_USUARIO_HORARIO_TUTOR, user.availableHours)
            put(COL_USUARIO_UBICACION_TUTOR, user.lugarReunion)
        }
        val result = db.insert(TABLA_USUARIOS, null, cv)
        exportarBaseDeDatos()
        return result != -1L
    }

    fun validarLogin(email: String, contrasena: String): User? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLA_USUARIOS WHERE $COL_USUARIO_CORREO = ? AND $COL_USUARIO_CONTRASENA = ?", arrayOf(email, contrasena))
        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                id = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_NOMBRE)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_CORREO)),
                password = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_CONTRASENA)) ?: "",
                career = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_CARRERA)),
                cuatrimestre = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_CUATRIMESTRE)),
                isTutor = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USUARIO_ES_TUTOR)) == 1,
                hourlyRate = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USUARIO_TARIFA)),
                cuatrimestresTutor = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_CUATRIS_TUTOR)) ?: "",
                materiasTutor = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_MATERIAS_TUTOR)) ?: "",
                bio = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_BIO_TUTOR)) ?: "Tutor disponible.",
                availableDays = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_DIAS_TUTOR)) ?: "Lun,Mar,Mié,Jue,Vie",
                availableHours = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_HORARIO_TUTOR)) ?: "09:00 a.m. - 06:00 p.m.",
                lugarReunion = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_UBICACION_TUTOR)) ?: "Biblioteca Universitaria, Edificio B, Sala 3"
            )
        }
        cursor.close()
        return user
    }

    fun getUsuarioById(userId: String): User {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLA_USUARIOS WHERE $COL_USUARIO_ID = ?", arrayOf(userId))
        var user = User()
        if (cursor.moveToFirst()) {
            user = User(
                id = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_NOMBRE)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_CORREO)),
                password = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_CONTRASENA)) ?: "",
                career = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_CARRERA)),
                cuatrimestre = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_CUATRIMESTRE)),
                isTutor = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USUARIO_ES_TUTOR)) == 1,
                hourlyRate = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USUARIO_TARIFA)),
                cuatrimestresTutor = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_CUATRIS_TUTOR)) ?: "",
                materiasTutor = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_MATERIAS_TUTOR)) ?: "",
                bio = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_BIO_TUTOR)) ?: "Tutor disponible.",
                availableDays = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_DIAS_TUTOR)) ?: "Lun,Mar,Mié,Jue,Vie",
                availableHours = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_HORARIO_TUTOR)) ?: "09:00 a.m. - 06:00 p.m.",
                lugarReunion = cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_UBICACION_TUTOR)) ?: "Biblioteca Universitaria, Edificio B, Sala 3"
            )
        }
        cursor.close()
        return user
    }

    fun actualizarUsuario(user: User) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COL_USUARIO_NOMBRE, user.name)
            put(COL_USUARIO_CORREO, user.email)
            put(COL_USUARIO_CONTRASENA, user.password)
            put(COL_USUARIO_CARRERA, user.career)
            put(COL_USUARIO_CUATRIMESTRE, user.cuatrimestre)
            put(COL_USUARIO_ES_TUTOR, if (user.isTutor) 1 else 0)
            put(COL_USUARIO_TARIFA, user.hourlyRate)
            put(COL_USUARIO_CUATRIS_TUTOR, user.cuatrimestresTutor)
            put(COL_USUARIO_MATERIAS_TUTOR, user.materiasTutor)
            put(COL_USUARIO_BIO_TUTOR, user.bio)
            put(COL_USUARIO_DIAS_TUTOR, user.availableDays)
            put(COL_USUARIO_HORARIO_TUTOR, user.availableHours)
            put(COL_USUARIO_UBICACION_TUTOR, user.lugarReunion)
        }
        db.update(TABLA_USUARIOS, cv, "$COL_USUARIO_ID = ?", arrayOf(user.id))

        if (user.isTutor) {
            val tutorCv = ContentValues().apply {
                put("id", user.id)
                put("nombre", user.name)
                put("carrera", user.career)
                put("semestre", "${user.cuatrimestre} (Imparte: ${user.cuatrimestresTutor})")
                put("calificacion", 5.0)
                put("numero_resenas", 1)
                put("biografia", user.bio)
                put("tarifa_hora", user.hourlyRate)
                put("materias", user.materiasTutor)
                put("dias_disponibles", user.availableDays)
                put("horario", user.availableHours)
                put("ubicacion", user.lugarReunion)
            }
            db.insertWithOnConflict(TABLA_TUTORES, null, tutorCv, SQLiteDatabase.CONFLICT_REPLACE)
        }

        exportarBaseDeDatos()
    }

    fun getAllTutors(): List<Tutor> {
        val list = mutableListOf<Tutor>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLA_TUTORES", null)
        if (cursor.moveToFirst()) {
            do {
                val materiasStr = cursor.getString(cursor.getColumnIndexOrThrow("materias")) ?: "General"
                val diasStr = cursor.getString(cursor.getColumnIndexOrThrow("dias_disponibles")) ?: "Lun,Mar,Mié,Jue,Vie"
                val ubiStr = try { cursor.getString(cursor.getColumnIndexOrThrow("ubicacion")) ?: "Biblioteca Universitaria" } catch (e: Exception) { "Biblioteca Universitaria, Edificio B, Sala 3" }
                list.add(
                    Tutor(
                        id = cursor.getString(cursor.getColumnIndexOrThrow("id")),
                        name = cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                        career = cursor.getString(cursor.getColumnIndexOrThrow("carrera")),
                        semester = cursor.getString(cursor.getColumnIndexOrThrow("semestre")),
                        rating = cursor.getDouble(cursor.getColumnIndexOrThrow("calificacion")),
                        reviewsCount = cursor.getInt(cursor.getColumnIndexOrThrow("numero_resenas")),
                        bio = cursor.getString(cursor.getColumnIndexOrThrow("biografia")),
                        hourlyRate = cursor.getInt(cursor.getColumnIndexOrThrow("tarifa_hora")),
                        subjects = materiasStr.split(",").map { it.trim() },
                        availableDays = diasStr.split(",").map { it.trim() },
                        availableHours = cursor.getString(cursor.getColumnIndexOrThrow("horario")),
                        location = ubiStr
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        exportarBaseDeDatos()
        return list
    }

    fun getSessionsForUser(userId: String, userName: String): List<TutoringSession> {
        val list = mutableListOf<TutoringSession>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLA_TUTORIAS WHERE $COL_SESION_ALUMNO_ID = ? OR $COL_SESION_TUTOR = ?",
            arrayOf(userId, userName)
        )
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    TutoringSession(
                        id = cursor.getString(cursor.getColumnIndexOrThrow(COL_SESION_ID)),
                        tutorName = cursor.getString(cursor.getColumnIndexOrThrow(COL_SESION_TUTOR)),
                        studentId = cursor.getString(cursor.getColumnIndexOrThrow(COL_SESION_ALUMNO_ID)) ?: userId,
                        studentName = cursor.getString(cursor.getColumnIndexOrThrow(COL_SESION_ALUMNO_NOMBRE)) ?: "Alumno",
                        subject = cursor.getString(cursor.getColumnIndexOrThrow(COL_SESION_MATERIA)),
                        modality = Modality.PRESENTIAL,
                        date = cursor.getString(cursor.getColumnIndexOrThrow(COL_SESION_FECHA)),
                        time = cursor.getString(cursor.getColumnIndexOrThrow(COL_SESION_HORA)),
                        status = cursor.getString(cursor.getColumnIndexOrThrow(COL_SESION_ESTADO)) ?: "PENDIENTE",
                        location = cursor.getString(cursor.getColumnIndexOrThrow(COL_SESION_UBICACION)) ?: "Biblioteca Universitaria, Edificio B, Sala 3"
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getBookedTimes(tutorName: String, date: String): List<String> {
        val list = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COL_SESION_HORA FROM $TABLA_TUTORIAS WHERE $COL_SESION_TUTOR = ? AND $COL_SESION_FECHA = ? AND $COL_SESION_ESTADO != 'CANCELADA'",
            arrayOf(tutorName, date)
        )
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun insertSession(session: TutoringSession) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COL_SESION_ID, session.id)
            put(COL_SESION_TUTOR, session.tutorName)
            put(COL_SESION_ALUMNO_ID, session.studentId)
            put(COL_SESION_ALUMNO_NOMBRE, session.studentName)
            put(COL_SESION_MATERIA, session.subject)
            put(COL_SESION_MODALIDAD, Modality.PRESENTIAL.name)
            put(COL_SESION_FECHA, session.date)
            put(COL_SESION_HORA, session.time)
            put(COL_SESION_ESTADO, session.status)
            put(COL_SESION_UBICACION, session.location)
        }
        db.insert(TABLA_TUTORIAS, null, cv)

        // Notificación al Alumno
        insertarNotificacion(NotificationItem(
            id = System.currentTimeMillis().toString() + "_al",
            userId = session.studentId,
            title = "Tutoría solicitada",
            message = "Solicitud enviada a ${session.tutorName} (${session.subject}).",
            timestamp = "Hoy"
        ))

        // Notificación al Tutor
        insertarNotificacion(NotificationItem(
            id = System.currentTimeMillis().toString() + "_tu",
            userId = session.tutorName,
            title = "Nueva solicitud de tutoría",
            message = "El alumno ${session.studentName} te solicitó la tutoría de ${session.subject}.",
            timestamp = "Hoy"
        ))

        exportarBaseDeDatos()
    }

    fun confirmarSesion(sessionId: String) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COL_SESION_ESTADO, "CONFIRMADA")
        }
        db.update(TABLA_TUTORIAS, cv, "$COL_SESION_ID = ?", arrayOf(sessionId))

        val cursor = db.rawQuery("SELECT $COL_SESION_ALUMNO_ID, $COL_SESION_MATERIA, $COL_SESION_TUTOR FROM $TABLA_TUTORIAS WHERE $COL_SESION_ID = ?", arrayOf(sessionId))
        var studentId = "1"
        var subject = "Materia"
        var tutorName = "Tutor"
        if (cursor.moveToFirst()) {
            studentId = cursor.getString(0) ?: "1"
            subject = cursor.getString(1) ?: "Materia"
            tutorName = cursor.getString(2) ?: "Tutor"
        }
        cursor.close()

        insertarNotificacion(NotificationItem(
            id = System.currentTimeMillis().toString(),
            userId = studentId,
            title = "Tutoría confirmada",
            message = "El tutor $tutorName confirmó tu tutoría de $subject.",
            timestamp = "Hoy"
        ))

        exportarBaseDeDatos()
    }

    fun cancelarSesion(sessionId: String) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COL_SESION_ESTADO, "CANCELADA")
        }
        db.update(TABLA_TUTORIAS, cv, "$COL_SESION_ID = ?", arrayOf(sessionId))

        val cursor = db.rawQuery("SELECT $COL_SESION_ALUMNO_ID, $COL_SESION_MATERIA FROM $TABLA_TUTORIAS WHERE $COL_SESION_ID = ?", arrayOf(sessionId))
        var studentId = "1"
        if (cursor.moveToFirst()) {
            studentId = cursor.getString(0) ?: "1"
        }
        cursor.close()

        insertarNotificacion(NotificationItem(
            id = System.currentTimeMillis().toString(),
            userId = studentId,
            title = "Tutoría cancelada",
            message = "La tutoría ha sido cancelada.",
            timestamp = "Hoy"
        ))

        exportarBaseDeDatos()
    }

    fun getMensajes(sessionId: String): List<Message> {
        val list = mutableListOf<Message>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLA_MENSAJES WHERE $COL_MENSAJE_SESION_ID = ?", arrayOf(sessionId))
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Message(
                        id = cursor.getString(cursor.getColumnIndexOrThrow(COL_MENSAJE_ID)),
                        tutoringSessionId = cursor.getString(cursor.getColumnIndexOrThrow(COL_MENSAJE_SESION_ID)),
                        senderName = cursor.getString(cursor.getColumnIndexOrThrow(COL_MENSAJE_EMISOR)),
                        content = cursor.getString(cursor.getColumnIndexOrThrow(COL_MENSAJE_CONTENIDO)),
                        timestamp = cursor.getString(cursor.getColumnIndexOrThrow(COL_MENSAJE_FECHA))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun insertarMensaje(message: Message) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COL_MENSAJE_ID, message.id)
            put(COL_MENSAJE_SESION_ID, message.tutoringSessionId)
            put(COL_MENSAJE_EMISOR, message.senderName)
            put(COL_MENSAJE_CONTENIDO, message.content)
            put(COL_MENSAJE_FECHA, message.timestamp)
        }
        db.insert(TABLA_MENSAJES, null, cv)

        exportarBaseDeDatos()
    }

    fun getNotificaciones(userId: String, userName: String = ""): List<NotificationItem> {
        val list = mutableListOf<NotificationItem>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLA_NOTIFICACIONES WHERE $COL_NOTIF_USUARIO_ID = ? OR $COL_NOTIF_USUARIO_ID = ? ORDER BY $COL_NOTIF_ID DESC",
            arrayOf(userId, userName)
        )
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    NotificationItem(
                        id = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTIF_ID)),
                        userId = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTIF_USUARIO_ID)) ?: userId,
                        title = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTIF_TITULO)),
                        message = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTIF_MENSAJE)),
                        timestamp = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOTIF_FECHA)),
                        read = cursor.getInt(cursor.getColumnIndexOrThrow(COL_NOTIF_LEIDA)) == 1
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    private fun insertarNotificacion(notif: NotificationItem) {
        val db = writableDatabase
        val cv = ContentValues().apply {
            put(COL_NOTIF_ID, notif.id)
            put(COL_NOTIF_USUARIO_ID, notif.userId)
            put(COL_NOTIF_TITULO, notif.title)
            put(COL_NOTIF_MENSAJE, notif.message)
            put(COL_NOTIF_FECHA, notif.timestamp)
            put(COL_NOTIF_LEIDA, if (notif.read) 1 else 0)
        }
        db.insert(TABLA_NOTIFICACIONES, null, cv)
    }

    private fun exportarBaseDeDatos() {
        try {
            val dbFile = context.getDatabasePath(DATABASE_NAME)
            val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (dbFile.exists()) {
                if (!downloadDir.exists()) downloadDir.mkdirs()
                val exportFile = File(downloadDir, "prendecon.db")
                dbFile.copyTo(exportFile, overwrite = true)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
