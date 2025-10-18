package dev.koenv.libraryapi.mappers.user

import dev.koenv.libraryapi.domain.entity.User
import dev.koenv.libraryapi.dto.user.UpdateUserDto
import dev.koenv.libraryapi.shared.util.PasswordUtil
import java.util.*

fun UpdateUserDto.toEntity(id: UUID, existing: User): User {
    return existing.copy(
        id = id,
        email = this.email ?: existing.email,
        passwordHash = this.password?.let { PasswordUtil.hash(it) } ?: existing.passwordHash,
        role = existing.role // immutable through update DTO
    )
}
