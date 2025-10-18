package dev.koenv.libraryapi.routes.api.v1.users

import dev.koenv.libraryapi.domain.policy.UserPolicies.requireCanDeleteUser
import dev.koenv.libraryapi.domain.policy.UserPolicies.requireCanReadUser
import dev.koenv.libraryapi.domain.policy.UserPolicies.requireCanUpdateRole
import dev.koenv.libraryapi.domain.policy.UserPolicies.requireCanUpdateUser
import dev.koenv.libraryapi.domain.service.UserService
import dev.koenv.libraryapi.dto.user.UpdateUserDto
import dev.koenv.libraryapi.dto.user.UpdateUserRoleDto
import dev.koenv.libraryapi.enums.Permission
import dev.koenv.libraryapi.mappers.user.toDto
import dev.koenv.libraryapi.routes.RouteRegistrar
import dev.koenv.libraryapi.shared.auth.AuthContext.requirePermission
import dev.koenv.libraryapi.shared.auth.AuthContext.requireUser
import dev.koenv.libraryapi.shared.http.RequestUtil.requireBody
import dev.koenv.libraryapi.shared.http.RequestUtil.requireUuidParam
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

object UserRoutes : RouteRegistrar {
    override fun Route.register() {
        val service by inject<UserService>()

        route("/users") {
            authenticate("auth-jwt") {

                get {
                    call.requirePermission(Permission.USER_READ_OTHERS)
                    call.respond(service.list().map { it.toDto() })
                }

                get("/me") {
                    val id = call.requireUser()
                    call.requireCanReadUser(id)
                    call.respond(HttpStatusCode.OK, service.get(id).toDto())
                }

                get("/{id}") {
                    val id = call.requireUuidParam("id")
                    call.requireCanReadUser(id)
                    call.respond(HttpStatusCode.OK, service.get(id).toDto())
                }

                put("/{id}") {
                    val id = call.requireUuidParam("id")
                    call.requireCanUpdateUser(id)
                    val body = call.requireBody<UpdateUserDto>()
                    val updated = service.update(id, body)
                    call.respond(HttpStatusCode.OK, updated.toDto())
                }

                patch("/{id}/role") {
                    val id = call.requireUuidParam("id")
                    call.requireCanUpdateRole()
                    val body = call.requireBody<UpdateUserRoleDto>()
                    val updated = service.updateRole(id, body)
                    call.respond(HttpStatusCode.OK, updated.toDto())
                }

                delete("/{id}") {
                    val id = call.requireUuidParam("id")
                    call.requireCanDeleteUser(id)
                    service.delete(id)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}
