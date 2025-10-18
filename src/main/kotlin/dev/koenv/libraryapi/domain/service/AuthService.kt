package dev.koenv.libraryapi.domain.service

import dev.koenv.libraryapi.domain.entity.User
import dev.koenv.libraryapi.domain.repository.UserRepository
import dev.koenv.libraryapi.dto.auth.*
import dev.koenv.libraryapi.mappers.auth.toEntity
import dev.koenv.libraryapi.mappers.user.toDto
import dev.koenv.libraryapi.shared.util.PasswordUtil
import io.ktor.server.config.*
import java.util.UUID

class AuthService(
    private val repo: UserRepository,
    private val config: ApplicationConfig,
    private val sessions: SessionService
) {
    suspend fun register(req: RegisterRequestDto): AuthResponseDto {
        validateRegistration(req.email, req.password)

        if (repo.findByEmail(req.email) != null)
            throw IllegalArgumentException("Email already registered")

        val hash = PasswordUtil.hash(req.password)
        val created = repo.create(req.toEntity(hash))

        val pair = sessions.createSession(created)
        return AuthResponseDto(pair.accessToken, pair.refreshToken, created.toDto())
    }

    suspend fun authenticate(email: String, password: String): User {
        val user = repo.findByEmail(email)
            ?: throw IllegalArgumentException("Invalid credentials")

        if (!PasswordUtil.verify(password, user.passwordHash))
            throw IllegalArgumentException("Invalid credentials")

        return user
    }

    suspend fun getUserById(id: UUID) =
        repo.findById(id)?.toDto() ?: throw IllegalArgumentException("User not found")

    private fun validateRegistration(email: String, password: String) {
        require(email.contains("@")) { "Invalid email format" }
        require(password.length >= 8) { "Password must be at least 8 characters" }
        require(password.any { it.isDigit() }) { "Password must contain at least one digit" }
        require(password.any { it.isUpperCase() }) { "Password must contain at least one uppercase letter" }
        require(password.any { it.isLowerCase() }) { "Password must contain at least one lowercase letter" }
        require(password.any { !it.isLetterOrDigit() }) { "Password must contain at least one special character" }
    }
}
