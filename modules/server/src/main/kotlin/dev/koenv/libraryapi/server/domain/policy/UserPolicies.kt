package dev.koenv.libraryapi.server.domain.policy

import dev.koenv.libraryapi.server.enums.Permission
import dev.koenv.libraryapi.server.shared.auth.AuthContext.requirePermission
import dev.koenv.libraryapi.server.shared.auth.AuthContext.requireUser
import io.ktor.server.application.*
import java.util.*

object UserPolicies {

    fun ApplicationCall.requireCanReadUser(targetId: UUID) {
        val currentId = requireUser()
        val same = currentId == targetId
        if (same) requirePermission(Permission.USER_READ_SELF)
        else requirePermission(Permission.USER_READ_OTHERS)
    }

    fun ApplicationCall.requireCanUpdateUser(targetId: UUID) {
        val currentId = requireUser()
        val same = currentId == targetId
        if (same) requirePermission(Permission.USER_UPDATE_SELF)
        else requirePermission(Permission.USER_UPDATE_OTHERS)
    }

    fun ApplicationCall.requireCanDeleteUser(targetId: UUID) {
        val currentId = requireUser()
        val same = currentId == targetId
        if (same) requirePermission(Permission.USER_DELETE_SELF)
        else requirePermission(Permission.USER_DELETE_OTHERS)
    }

    fun ApplicationCall.requireCanUpdateRole() =
        requirePermission(Permission.USER_ROLE_UPDATE)
}
