package dev.koenv.libraryapi.domain.service

import dev.koenv.libraryapi.domain.entity.User
import dev.koenv.libraryapi.domain.repository.UserRepository
import java.util.*

class UserService(private val repo: UserRepository) {

    suspend fun getAll(): List<User> = repo.findAll()

    suspend fun getById(id: UUID): User? = repo.findById(id)

    suspend fun create(user: User): User = repo.create(user)

    suspend fun update(id: UUID, user: User): User? = repo.update(id, user)

    suspend fun delete(id: UUID): Boolean = repo.delete(id)
}
