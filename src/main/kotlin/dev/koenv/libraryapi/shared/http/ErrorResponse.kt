package dev.koenv.libraryapi.shared.http

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val success: Boolean = false,
    val code: String,
    val message: String,
    val status: Int,
    val traceId: String? = null,
    val details: Map<String, String>? = null
)
