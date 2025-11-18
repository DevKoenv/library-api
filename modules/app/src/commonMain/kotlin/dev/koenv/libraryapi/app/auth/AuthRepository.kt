package dev.koenv.libraryapi.app.auth

import dev.koenv.libraryapi.app.platform.PlatformLog
import kotlin.random.Random

/**
 * In-memory user store. Not persisted. Thread-safe enough for demo.
 */
internal object AuthRepository {
    private const val TAG = "AuthRepo"

    // Seed user that is allowed to log in out of the box.
    // email: demo@library.local  password: demo
    private val users = mutableListOf(
        UserRecord(id = "admin_demo", email = "admin@library.local", password = "demo", role = "admin"),
        UserRecord(id = "user_demo", email = "user@library.local", password = "demo", role = "user")
    )

    fun tryLogin(email: String, password: String): User? {
        PlatformLog.d(TAG, "Login attempt email=$email (dummy)")
        val rec = users.firstOrNull { it.email.equals(email.trim(), ignoreCase = true) }
        if (rec != null && rec.password == password) {
            PlatformLog.d(TAG, "Login success for $email")
            return User(id = rec.id, email = rec.email, role = rec.role)
        }
        PlatformLog.d(TAG, "Login failed for $email")
        return null
    }

    fun tryRegister(email: String, password: String): Result<User> {
        PlatformLog.d(TAG, "Register attempt email=$email (dummy)")
        val cleanEmail = email.trim()
        if (cleanEmail.isEmpty() || password.isEmpty()) return Result.failure(IllegalArgumentException("Empty email or password"))
        if (users.any { it.email.equals(cleanEmail, ignoreCase = true) }) {
            PlatformLog.d(TAG, "Register failed, duplicate email=$cleanEmail")
            return Result.failure(IllegalStateException("Email already exists"))
        }
        val id = genId()
        val role = "user"
        val rec = UserRecord(id = id, email = cleanEmail, password = password, role = role)
        users += rec
        PlatformLog.d(TAG, "Register success id=$id email=$cleanEmail")
        return Result.success(User(id = id, email = cleanEmail, role = role))
    }

    fun listUsers(): List<User> = users.map { User(it.id, it.email, it.role) }

    private fun genId(): String {
        val alphabet = "abcdefghijklmnopqrstuvwxyz0123456789"
        val core = (1..12).map { alphabet[Random.nextInt(alphabet.length)] }.joinToString("")
        return "u_$core"
    }
}
