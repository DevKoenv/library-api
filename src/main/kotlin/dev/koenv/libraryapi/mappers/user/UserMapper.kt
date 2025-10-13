package dev.koenv.libraryapi.mappers.user

import dev.koenv.libraryapi.domain.entity.User
import dev.koenv.libraryapi.dto.user.UserDto

fun User.toDto(): UserDto = UserDto(
    id = id ?: throw IllegalStateException("User ID is null"),
    email = email,
    role = role
)
