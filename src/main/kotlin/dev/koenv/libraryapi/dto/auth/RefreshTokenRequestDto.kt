package dev.koenv.libraryapi.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequestDto(
    val refreshToken: String
)
