package dev.koenv.libraryapi.server.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequestDto(
    val refreshToken: String
)
