package dev.koenv.libraryapi.dto.user

import dev.koenv.libraryapi.domain.entity.Role
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRoleDto(
    val role: Role
)
