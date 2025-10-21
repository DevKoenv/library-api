package dev.koenv.libraryapi.mappers.user

import dev.koenv.libraryapi.domain.entity.User
import dev.koenv.libraryapi.dto.user.*
import dev.koenv.libraryapi.shared.auth.PasswordUtil

fun User.toDto(): UserDto = UserDto(
    id = requireNotNull(id) { "User.id must be set when mapping to DTO" },
    email = email,
    role = role
)

fun UpdateUserDto.applyTo(existing: User): User = existing.copy(
    email = email ?: existing.email,
    passwordHash = password?.let { PasswordUtil.hash(it) } ?: existing.passwordHash,
    role = existing.role
)
