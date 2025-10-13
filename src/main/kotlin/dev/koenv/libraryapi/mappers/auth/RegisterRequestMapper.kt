package dev.koenv.libraryapi.mappers.auth

import dev.koenv.libraryapi.domain.entity.Role
import dev.koenv.libraryapi.domain.entity.User
import dev.koenv.libraryapi.dto.auth.RegisterRequestDto

fun RegisterRequestDto.toEntity(passwordHash: String): User = User(
    email = email,
    passwordHash = passwordHash,
    role = Role.DRIVER
)
