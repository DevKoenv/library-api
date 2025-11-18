package dev.koenv.libraryapi.server.domain.entity

import dev.koenv.libraryapi.server.domain.enums.Role
import dev.koenv.libraryapi.server.shared.serialization.UUIDSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class User(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = null,
    val email: String,
    val passwordHash: String,
    val role: Role = Role.MEMBER,
    val createdAt: LocalDateTime? = null
)
