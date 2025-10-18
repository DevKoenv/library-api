package dev.koenv.libraryapi.routes.api.v1.users

import dev.koenv.libraryapi.enums.Permission
import dev.koenv.libraryapi.domain.service.UserService
import dev.koenv.libraryapi.dto.user.UpdateUserDto
import dev.koenv.libraryapi.mappers.user.toDto
import dev.koenv.libraryapi.mappers.user.toEntity
import dev.koenv.libraryapi.routes.RouteRegistrar
import dev.koenv.libraryapi.shared.http.ApiException
import dev.koenv.libraryapi.shared.util.*
import io.ktor.http.*
import io.ktor.server.auth.authenticate
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

object UserRoutes : RouteRegistrar {
    override fun Route.register() {
        val userService by inject<UserService>()

        route("/users") {
            authenticate("auth-jwt") {
                get {
                    call.requirePermission(Permission.USER_READ_ALL)
                    call.respond(userService.getAll().map { it.toDto() })
                }

                get("/{id}") {
                    call.requirePermission(Permission.USER_READ_SELF, Permission.USER_READ_ALL)
                    val targetId = call.requireUuidParamOrFail("id")
                    val currentUserId = call.requireUser()
                    val readingSelf = currentUserId == targetId
                    val canReadAll = call.hasPermission(Permission.USER_READ_ALL)

                    if (!readingSelf && !canReadAll) throw ApiException(
                        HttpStatusCode.Forbidden,
                        message = "Cannot read other users"
                    )

                    val user = userService.getById(targetId) ?: throw ApiException(
                        HttpStatusCode.NotFound,
                        message = "User not found"
                    )
                    call.respond(HttpStatusCode.OK, user.toDto())

                }

                put("/{id}") {
                    call.requirePermission(Permission.USER_UPDATE_SELF, Permission.USER_UPDATE_OTHERS)

                    val targetId = call.requireUuidParamOrFail("id")
                    val currentUserId = call.requireUser()

                    val updatingSelf = currentUserId == targetId
                    val canUpdateOthers = call.hasPermission(Permission.USER_UPDATE_OTHERS)

                    if (!updatingSelf && !canUpdateOthers) throw ApiException(
                        HttpStatusCode.Forbidden,
                        message = "Cannot update other users"
                    )

                    val existing = userService.getById(targetId) ?: throw ApiException(
                        HttpStatusCode.NotFound,
                        message = "User not found"
                    )

                    val dto = call.receive<UpdateUserDto>()
                    val updatedEntity = dto.toEntity(targetId, existing)
                    val saved = userService.update(targetId, updatedEntity)
                        ?: throw ApiException(HttpStatusCode.InternalServerError, message = "Update failed")

                    call.respond(HttpStatusCode.OK, saved.toDto())
                }


                delete("/{id}") {
                    call.requirePermission(Permission.USER_DELETE_SELF, Permission.USER_DELETE_OTHERS)
                    val targetId = call.requireUuidParamOrFail("id")
                    val currentUserId = call.requireUser()
                    val deletingSelf = currentUserId == targetId
                    val canDeleteOthers = call.hasPermission(Permission.USER_DELETE_OTHERS)

                    if (!deletingSelf && !canDeleteOthers) throw ApiException(
                        HttpStatusCode.Forbidden,
                        message = "Cannot delete other users"
                    )

                    userService.delete(targetId)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}
