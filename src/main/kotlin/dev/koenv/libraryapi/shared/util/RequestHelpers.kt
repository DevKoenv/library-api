package dev.koenv.libraryapi.shared.util

import dev.koenv.libraryapi.domain.entity.Role
import dev.koenv.libraryapi.enums.Permission
import dev.koenv.libraryapi.shared.http.ApiException
import io.ktor.http.*
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.UUID
import io.ktor.server.application.ApplicationCall

class RequestAborted : RuntimeException()

fun JWTPrincipal.userIdOrNull(): UUID? {
    return runCatching { UUID.fromString(payload.getClaim("sub").asString()) }.getOrNull()
}

fun ApplicationCall.requireUser(): UUID {
    val principal = principal<JWTPrincipal>()
        ?: throw ApiException(HttpStatusCode.Unauthorized, message = "Missing JWT principal")

    val id = principal.userIdOrNull()
        ?: throw ApiException(HttpStatusCode.BadRequest, message = "Invalid or missing sub claim")

    return id
}

fun JWTPrincipal.requireRole(vararg allowed: Role) {
    val role = this.payload.getClaim("role").asString()?.let { Role.valueOf(it) }
    if (role == null || role !in allowed) throw ApiException(HttpStatusCode.Forbidden, message = "Insufficient role")
}

fun ApplicationCall.requireRole(vararg allowed: Role): JWTPrincipal {
    val principal = this.principal<JWTPrincipal>() ?: throw ApiException(
        HttpStatusCode.InternalServerError,
        message = "No principal"
    )
    principal.requireRole(*allowed)
    return principal
}

fun JWTPrincipal.hasPermission(vararg permissions: Permission): Boolean {
    val role = this.payload.getClaim("role").asString()
        ?.let { Role.valueOf(it) }
        ?: return false

    return permissions.any { it in role.permissions }
}

fun JWTPrincipal.requirePermission(vararg allowed: Permission) {
    if (!hasPermission(*allowed)) {
        throw ApiException(HttpStatusCode.Forbidden, message = "Insufficient permission")
    }
}

fun ApplicationCall.hasPermission(vararg permissions: Permission): Boolean {
    val principal = this.principal<JWTPrincipal>() ?: return false
    return principal.hasPermission(*permissions)
}

fun ApplicationCall.requirePermission(vararg allowed: Permission): JWTPrincipal {
    val principal = this.principal<JWTPrincipal>() ?: throw ApiException(
        HttpStatusCode.InternalServerError,
        message = "No principal"
    )
    principal.requirePermission(*allowed)
    return principal
}

suspend fun ApplicationCall.requireUuidParamOrFail(name: String): UUID {
    val s = parameters[name] ?: run {
        respond(HttpStatusCode.BadRequest, "Missing $name")
        throw RequestAborted()
    }
    return try {
        UUID.fromString(s)
    } catch (_: IllegalArgumentException) {
        respond(HttpStatusCode.BadRequest, "Invalid $name")
        throw RequestAborted()
    }
}

suspend inline fun <reified T : Any> ApplicationCall.requireBodyOrFail(): T {
    return try {
        receive<T>()
    } catch (_: Exception) {
        respond(HttpStatusCode.BadRequest, "Invalid request body")
        throw RequestAborted()
    }
}
