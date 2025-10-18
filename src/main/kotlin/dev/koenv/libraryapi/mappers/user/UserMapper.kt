package dev.koenv.libraryapi.mappers.user

import dev.koenv.libraryapi.domain.entity.User
import dev.koenv.libraryapi.dto.user.UserDto
import java.util.*

fun User.toDto(): UserDto = UserDto(
    id = id ?: UUID(0, 0),
    email = email,
    role = role
)
