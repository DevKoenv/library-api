package dev.koenv.libraryapi.dto.user

import dev.koenv.libraryapi.domain.enums.Role
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRoleDto(
    val role: Role
)
