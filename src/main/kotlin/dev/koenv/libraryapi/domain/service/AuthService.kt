package dev.koenv.libraryapi.domain.service

import dev.koenv.libraryapi.domain.repository.UserRepository
import dev.koenv.libraryapi.dto.auth.*
import dev.koenv.libraryapi.mappers.auth.toEntity
import dev.koenv.libraryapi.mappers.user.toDto
import dev.koenv.libraryapi.shared.util.JwtUtil
import dev.koenv.libraryapi.shared.util.PasswordUtil
import io.ktor.server.config.*

class AuthService(
    private val repo: UserRepository,
    private val config: ApplicationConfig
) {
    suspend fun register(req: RegisterRequestDto): AuthResponseDto {
        validateRegistration(req.email, req.password)

        if (repo.findByEmail(req.email) != null)
            throw IllegalArgumentException("Email already registered")

        val hash = PasswordUtil.hash(req.password)
        val created = repo.create(req.toEntity(hash))

        val token = JwtUtil.generateToken(
            userId = created.id!!,
            role = created.role,
            audience = config.property("jwt.audience").getString(),
            issuer = config.property("jwt.domain").getString(),
            secret = config.property("jwt.secret").getString()
        )

        return AuthResponseDto(token, created.toDto())
    }

    suspend fun login(req: LoginRequestDto): AuthResponseDto {
        val user = repo.findByEmail(req.email)
            ?: throw IllegalArgumentException("Invalid credentials")

        if (!PasswordUtil.verify(req.password, user.passwordHash))
            throw IllegalArgumentException("Invalid credentials")

        val token = JwtUtil.generateToken(
            userId = user.id!!,
            role = user.role,
            audience = config.property("jwt.audience").getString(),
            issuer = config.property("jwt.domain").getString(),
            secret = config.property("jwt.secret").getString()
        )

        return AuthResponseDto(token, user.toDto())
    }

    private fun validateRegistration(email: String, password: String) {
        require(email.contains("@")) { "Invalid email format" }
        require(password.length >= 8) { "Password must be at least 8 characters" }
    }
}
