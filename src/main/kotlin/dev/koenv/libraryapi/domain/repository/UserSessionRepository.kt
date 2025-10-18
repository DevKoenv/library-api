package dev.koenv.libraryapi.domain.repository

import dev.koenv.libraryapi.domain.entity.UserSession
import java.util.UUID

interface UserSessionRepository {
    suspend fun create(session: UserSession, refreshTokenHash: String): UserSession
    suspend fun findById(id: UUID): UserSession?
    suspend fun listByUser(userId: UUID): List<UserSession>
    suspend fun getRefreshHash(id: UUID): String?
    suspend fun revoke(id: UUID): Boolean
    suspend fun revokeAllByUser(userId: UUID): Int
}
