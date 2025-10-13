package dev.koenv.libraryapi.domain.repository

import dev.koenv.libraryapi.domain.entity.User
import java.util.UUID

interface UserRepository : Repository<User, UUID> {
    suspend fun findByEmail(email: String): User?
}
