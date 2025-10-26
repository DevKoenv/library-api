package dev.koenv.libraryapi.dto.user

import dev.koenv.libraryapi.domain.enums.Role
import dev.koenv.libraryapi.shared.serialization.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class UserDto(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val email: String,
    val role: Role
)
