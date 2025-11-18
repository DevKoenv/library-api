package dev.koenv.libraryapi.server.dto.auth

import dev.koenv.libraryapi.server.dto.user.UserDto
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponseDto(
    val accessToken: String,
    val refreshToken: String,
    val user: UserDto
)
