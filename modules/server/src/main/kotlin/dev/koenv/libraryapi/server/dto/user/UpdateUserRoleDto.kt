package dev.koenv.libraryapi.server.dto.user

import dev.koenv.libraryapi.server.domain.enums.Role
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRoleDto(
    val role: Role
)
