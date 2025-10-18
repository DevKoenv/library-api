package dev.koenv.libraryapi.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class TokenPairDto(
    val accessToken: String,
    val refreshToken: String
)
