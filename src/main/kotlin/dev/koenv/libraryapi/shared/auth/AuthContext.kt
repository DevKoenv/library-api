package dev.koenv.libraryapi.shared.auth

import dev.koenv.libraryapi.domain.entity.Role
import dev.koenv.libraryapi.enums.Permission
import dev.koenv.libraryapi.shared.http.ApiException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.*

object AuthContext {

    // --- Extractors ---

    fun JWTPrincipal.userIdOrNull(): UUID? =
        runCatching { UUID.fromString(payload.getClaim("sub").asString()) }.getOrNull()

    fun ApplicationCall.requireUser(): UUID {
        val principal = principal<JWTPrincipal>()
            ?: throw ApiException(HttpStatusCode.Unauthorized, message = "Missing JWT principal")
        return principal.userIdOrNull()
            ?: throw ApiException(HttpStatusCode.BadRequest, message = "Invalid or missing sub claim")
    }

    // --- Role / Permission checks ---

    fun JWTPrincipal.roleOrNull(): Role? =
        payload.getClaim("role").asString()?.let { Role.valueOf(it) }

    fun JWTPrincipal.hasPermission(vararg permissions: Permission): Boolean {
        val role = roleOrNull() ?: return false
        return permissions.any { it in role.permissions }
    }

    fun ApplicationCall.hasPermission(vararg permissions: Permission): Boolean {
        val principal = principal<JWTPrincipal>() ?: return false
        return principal.hasPermission(*permissions)
    }

    fun ApplicationCall.requirePermission(vararg required: Permission) {
        val principal = principal<JWTPrincipal>()
            ?: throw ApiException(HttpStatusCode.InternalServerError, message = "Missing principal")
        val has = principal.hasPermission(*required)
        ensure(has, "Insufficient permission")
    }

    fun ensure(condition: Boolean, message: String) {
        if (!condition)
            throw ApiException(HttpStatusCode.Forbidden, message = message)
    }
}
