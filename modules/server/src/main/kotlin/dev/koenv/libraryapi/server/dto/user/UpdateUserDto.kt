package dev.koenv.libraryapi.server.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserDto(
    val email: String? = null,
    val password: String? = null
)
