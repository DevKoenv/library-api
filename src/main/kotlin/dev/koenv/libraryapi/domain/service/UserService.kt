package dev.koenv.libraryapi.domain.service

import dev.koenv.libraryapi.domain.entity.User
import dev.koenv.libraryapi.domain.repository.UserRepository
import dev.koenv.libraryapi.dto.user.UpdateUserDto
import dev.koenv.libraryapi.dto.user.UpdateUserRoleDto
import dev.koenv.libraryapi.shared.auth.PasswordUtil
import dev.koenv.libraryapi.shared.http.ApiException
import io.ktor.http.*
import java.util.*

class UserService(private val repo: UserRepository) {

    suspend fun list(): List<User> = repo.findAll()

    suspend fun get(id: UUID): User =
        repo.findById(id) ?: throw ApiException(HttpStatusCode.NotFound, message = "User not found")

    suspend fun update(id: UUID, body: UpdateUserDto): User {
        val existing = get(id)

        val email = body.email ?: existing.email
        val passwordHash = when (body.password) {
            null -> existing.passwordHash
            else -> PasswordUtil.hash(body.password)
        }

        val updated = existing.copy(email = email, passwordHash = passwordHash)
        return repo.update(id, updated)
            ?: throw ApiException(HttpStatusCode.InternalServerError, message = "Failed to update user")
    }

    suspend fun updateRole(id: UUID, body: UpdateUserRoleDto): User {
        val existing = get(id)
        val updated = existing.copy(role = body.role)
        return repo.update(id, updated)
            ?: throw ApiException(HttpStatusCode.InternalServerError, message = "Failed to update role")
    }

    suspend fun delete(id: UUID) {
        if (!repo.delete(id)) throw ApiException(HttpStatusCode.NotFound, message = "User not found")
    }
}
