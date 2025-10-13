package dev.koenv.libraryapi.domain.entity

import dev.koenv.libraryapi.shared.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime
import java.util.*

@Serializable
data class User(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = null,
    val email: String,
    val passwordHash: String,
    val role: Role = Role.DRIVER,
    val createdAt: LocalDateTime? = null
)
