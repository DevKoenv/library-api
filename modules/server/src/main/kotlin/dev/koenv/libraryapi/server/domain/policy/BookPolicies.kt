package dev.koenv.libraryapi.server.domain.policy

import dev.koenv.libraryapi.server.enums.Permission
import dev.koenv.libraryapi.server.shared.auth.AuthContext.requirePermission
import io.ktor.server.application.*

object BookPolicies {

    fun ApplicationCall.requireCanListBooks() =
        requirePermission(Permission.BOOK_READ)

    fun ApplicationCall.requireCanCreateBook() =
        requirePermission(Permission.BOOK_CREATE)

    fun ApplicationCall.requireCanUpdateBook() =
        requirePermission(Permission.BOOK_UPDATE)

    fun ApplicationCall.requireCanDeleteBook() =
        requirePermission(Permission.BOOK_DELETE)
}
