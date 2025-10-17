package dev.koenv.libraryapi.shared.util

import dev.koenv.libraryapi.domain.entity.Role
import dev.koenv.libraryapi.enums.Permission
import dev.koenv.libraryapi.shared.http.ApiException
import io.ktor.http.*

fun Role.require(permission: Permission) {
    if (permission !in this.permissions) {
        throw ApiException(
            http = HttpStatusCode.Forbidden,
            code = "MISSING_PERMISSION",
            message = "Missing permission: ${permission.name}"
        )
    }
}

fun Role.has(permission: Permission): Boolean = permission in this.permissions
