@file:Suppress("DEPRECATION", "DEPRECATION_ERROR")
package dev.koenv.libraryapi.domain.service

import dev.koenv.libraryapi.domain.entity.User
import dev.koenv.libraryapi.domain.entity.UserSession
import dev.koenv.libraryapi.domain.repository.UserRepository
import dev.koenv.libraryapi.domain.repository.UserSessionRepository
import dev.koenv.libraryapi.dto.auth.TokenPairDto
import dev.koenv.libraryapi.shared.util.JwtUtil
import dev.koenv.libraryapi.shared.util.PasswordUtil
import io.ktor.server.config.*
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.plus
import java.security.SecureRandom
import java.util.Base64
import java.util.UUID

class SessionService(
    private val repo: UserSessionRepository,
    private val userRepo: UserRepository,
    private val appConfig: ApplicationConfig
) {
    private val accessTtlSeconds = 24 * 3600L
    private val refreshTtlDays = 30L

    suspend fun createSession(user: User): TokenPairDto {
        val sessionId = UUID.randomUUID()
        val refreshSecret = randomSecret()
        val refreshToken = "${sessionId}.$refreshSecret"
        val refreshHash = PasswordUtil.hash(refreshToken)

        val nowInstant = Clock.System.now()
        val createdAt = nowInstant.toLocalDateTime(TimeZone.UTC)
        val expiresAt = nowInstant
            .plus(refreshTtlDays, DateTimeUnit.DAY, TimeZone.UTC)
            .toLocalDateTime(TimeZone.UTC)

        val session = UserSession(
            id = sessionId,
            userId = user.id!!,
            createdAt = createdAt,
            expiresAt = expiresAt,
            revokedAt = null
        )

        repo.create(session, refreshHash)

        val access = JwtUtil.generateAccessToken(
            userId = user.id,
            sessionId = sessionId,
            role = user.role,
            audience = appConfig.property("jwt.audience").getString(),
            issuer = appConfig.property("jwt.domain").getString(),
            secret = appConfig.property("jwt.secret").getString(),
            expiresInSeconds = accessTtlSeconds
        )

        return TokenPairDto(accessToken = access, refreshToken = refreshToken)
    }

    suspend fun refresh(refreshToken: String): TokenPairDto {
        val parts = refreshToken.split('.', limit = 2)
        if (parts.size != 2) throw IllegalArgumentException("Invalid refresh token format")
        val sid = runCatching { UUID.fromString(parts[0]) }
            .getOrElse { throw IllegalArgumentException("Invalid session id") }
        val secret = parts[1]

        val session = repo.findById(sid) ?: throw IllegalArgumentException("Session not found")
        if (session.revokedAt != null) throw IllegalArgumentException("Session revoked")

        val nowInstant = Clock.System.now()
        val now = nowInstant.toLocalDateTime(TimeZone.UTC)
        if (now > session.expiresAt) throw IllegalArgumentException("Session expired")

        val storedHash = repo.getRefreshHash(sid)
            ?: throw IllegalArgumentException("Invalid session")
        if (!PasswordUtil.verify("$sid.$secret", storedHash))
            throw IllegalArgumentException("Invalid refresh token")

        repo.revoke(sid)

        val user = userRepo.findById(session.userId)
            ?: throw IllegalArgumentException("User not found")

        val newSessionId = UUID.randomUUID()
        val newSecret = randomSecret()
        val newRefresh = "$newSessionId.$newSecret"
        val newHash = PasswordUtil.hash(newRefresh)

        val expiresAt = nowInstant
            .plus(refreshTtlDays, DateTimeUnit.DAY, TimeZone.UTC)
            .toLocalDateTime(TimeZone.UTC)

        val newSession = UserSession(
            id = newSessionId,
            userId = session.userId,
            createdAt = now,
            expiresAt = expiresAt,
            revokedAt = null
        )
        repo.create(newSession, newHash)

        val access = JwtUtil.generateAccessToken(
            userId = user.id!!,
            sessionId = newSessionId,
            role = user.role,
            audience = appConfig.property("jwt.audience").getString(),
            issuer = appConfig.property("jwt.domain").getString(),
            secret = appConfig.property("jwt.secret").getString(),
            expiresInSeconds = accessTtlSeconds
        )
        return TokenPairDto(access, newRefresh)
    }


    suspend fun listSessions(userId: UUID) = repo.listByUser(userId)

    suspend fun logoutCurrent(sessionId: UUID?, userId: UUID) {
        if (sessionId == null) return
        val s = repo.findById(sessionId) ?: return
        if (s.userId != userId) return
        repo.revoke(sessionId)
    }

    suspend fun logoutById(targetSessionId: UUID, userId: UUID) {
        val s = repo.findById(targetSessionId) ?: return
        if (s.userId != userId) return
        repo.revoke(targetSessionId)
    }

    suspend fun logoutAll(userId: UUID) {
        repo.revokeAllByUser(userId)
    }

    private fun randomSecret(): String {
        val bytes = ByteArray(32)
        SecureRandom().nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }
}
