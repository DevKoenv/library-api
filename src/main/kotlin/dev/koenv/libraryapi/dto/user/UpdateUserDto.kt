package dev.koenv.libraryapi.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserDto(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null
)
