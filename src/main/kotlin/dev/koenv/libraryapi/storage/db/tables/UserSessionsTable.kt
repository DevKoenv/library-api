package dev.koenv.libraryapi.storage.db.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.datetime

object UserSessionsTable : Table("user_sessions") {
    val id = uuid("id").autoGenerate()
    val userId = reference("user_id", UsersTable.id)
    val refreshToken = varchar("refresh_token", 255)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val expiresAt = datetime("expires_at")
    val revokedAt = datetime("revoked_at").nullable()

    override val primaryKey = PrimaryKey(id)
}
