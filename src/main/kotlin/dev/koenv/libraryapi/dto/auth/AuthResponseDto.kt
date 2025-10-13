package dev.koenv.libraryapi.dto.auth

import dev.koenv.libraryapi.dto.user.UserDto
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(
    val token: String,
    val user: UserDto
)
