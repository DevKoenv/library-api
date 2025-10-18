package dev.koenv.libraryapi.domain.entity

import dev.koenv.libraryapi.shared.serialization.UUIDSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UserSession(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    val createdAt: LocalDateTime,
    val expiresAt: LocalDateTime,
    val revokedAt: LocalDateTime? = null
)
