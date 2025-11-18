package dev.koenv.libraryapi.server.dto.user

import dev.koenv.libraryapi.server.domain.enums.Role
import dev.koenv.libraryapi.server.shared.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UserDto(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val email: String,
    val role: Role
)
