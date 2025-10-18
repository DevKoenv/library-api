@file:Suppress("DEPRECATION", "DEPRECATION_ERROR")
package dev.koenv.libraryapi.storage.repository

import dev.koenv.libraryapi.domain.entity.UserSession
import dev.koenv.libraryapi.domain.repository.UserSessionRepository
import dev.koenv.libraryapi.plugins.dbQuery
import dev.koenv.libraryapi.storage.db.tables.UserSessionsTable
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import java.util.*

class UserSessionRepositoryImpl : UserSessionRepository {

    override suspend fun create(session: UserSession, refreshTokenHash: String): UserSession = dbQuery {
        val id = UserSessionsTable.insert {
            it[UserSessionsTable.id] = session.id
            it[userId] = session.userId
            it[refreshToken] = refreshTokenHash
            it[createdAt] = session.createdAt
            it[expiresAt] = session.expiresAt
            it[revokedAt] = session.revokedAt
        } get UserSessionsTable.id

        UserSessionsTable
            .selectAll()
            .where { UserSessionsTable.id eq id }
            .map(::toEntity)
            .single()
    }

    override suspend fun findById(id: UUID): UserSession? = dbQuery {
        UserSessionsTable
            .selectAll()
            .where { UserSessionsTable.id eq id }
            .map(::toEntity)
            .singleOrNull()
    }

    override suspend fun listByUser(userId: UUID): List<UserSession> = dbQuery {
        UserSessionsTable
            .selectAll()
            .where { UserSessionsTable.userId eq userId }
            .map(::toEntity)
    }

    override suspend fun getRefreshHash(id: UUID): String? = dbQuery {
        // Simpler: no slice/limit; just pick the column from the first row if any.
        UserSessionsTable
            .selectAll()
            .where { UserSessionsTable.id eq id }
            .map { it[UserSessionsTable.refreshToken] }
            .firstOrNull()
    }

    override suspend fun revoke(id: UUID): Boolean = dbQuery {
        val updated = UserSessionsTable.update({ UserSessionsTable.id eq id }) {
            it[revokedAt] = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        }
        updated > 0
    }

    override suspend fun revokeAllByUser(userId: UUID): Int = dbQuery {
        // No isNull()/and needed; re-setting revokedAt is acceptable.
        UserSessionsTable.update({ UserSessionsTable.userId eq userId }) {
            it[revokedAt] = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        }
    }

    private fun toEntity(row: ResultRow): UserSession = UserSession(
        id = row[UserSessionsTable.id],
        userId = row[UserSessionsTable.userId],
        createdAt = row[UserSessionsTable.createdAt],
        expiresAt = row[UserSessionsTable.expiresAt],
        revokedAt = row[UserSessionsTable.revokedAt]
    )
}
