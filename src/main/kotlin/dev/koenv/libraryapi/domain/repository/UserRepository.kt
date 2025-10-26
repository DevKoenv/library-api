package dev.koenv.libraryapi.domain.repository

import dev.koenv.libraryapi.domain.entity.User
import java.util.UUID

interface UserRepository {
    suspend fun findAll(): List<User>
    suspend fun findById(id: UUID): User?
    suspend fun existsById(id: UUID): Boolean

    suspend fun create(entity: User): User
    suspend fun update(id: UUID, entity: User): User?
    suspend fun delete(id: UUID): Boolean

    suspend fun count(): Long
    suspend fun findByEmail(email: String): User?
}
