package dev.koenv.libraryapi.app.auth

data class User(
    val id: String,
    val email: String,
    val role: String = "user",
)

internal data class UserRecord(
    val id: String,
    val email: String,
    val password: String,
    val role: String,
)
