package dev.koenv.libraryapi.server.mappers.auth

import dev.koenv.libraryapi.server.domain.enums.Role
import dev.koenv.libraryapi.server.domain.entity.User
import dev.koenv.libraryapi.server.dto.auth.RegisterRequestDto

fun RegisterRequestDto.toEntity(passwordHash: String): User = User(
    email = email,
    passwordHash = passwordHash,
    role = Role.MEMBER,
)
