package dev.koenv.libraryapi.server.domain.repository

import dev.koenv.libraryapi.server.domain.entity.UserSession
import java.util.*

interface UserSessionRepository {
    suspend fun create(session: UserSession, refreshTokenHash: String): UserSession
    suspend fun findById(id: UUID): UserSession?
    suspend fun listByUser(userId: UUID): List<UserSession>
    suspend fun getRefreshHash(id: UUID): String?
    suspend fun revoke(id: UUID): Boolean
    suspend fun revokeAllByUser(userId: UUID): Int
}
