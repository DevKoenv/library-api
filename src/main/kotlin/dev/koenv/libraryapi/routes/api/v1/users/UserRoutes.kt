package dev.koenv.libraryapi.routes.api.v1.users

import dev.koenv.libraryapi.domain.entity.Role
import dev.koenv.libraryapi.domain.service.UserService
import dev.koenv.libraryapi.mappers.user.toDto
import dev.koenv.libraryapi.routes.RouteRegistrar
import dev.koenv.libraryapi.shared.util.requireRole
import dev.koenv.libraryapi.shared.util.requireUuidParamOrFail
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import kotlin.getValue

object UserRoutes : RouteRegistrar {
    override fun Route.register() {
        val userService by inject<UserService>()

        route("/users") {
            authenticate("auth-jwt") {
                get {
                    call.requireRole(Role.ADMIN)
                    call.respond(userService.getAll().map { it.toDto() })
                }

                get("/{id}") {
                    call.requireRole(Role.ADMIN, Role.DRIVER)
                    val id = call.requireUuidParamOrFail("id")
                    val user = userService.getById(id)
                    if (user == null) call.respond(HttpStatusCode.NotFound)
                    else call.respond(user.toDto())
                }

                delete("/{id}") {
                    call.requireRole(Role.ADMIN)
                    val id = call.requireUuidParamOrFail("id")
                    if (userService.delete(id)) call.respond(HttpStatusCode.NoContent)
                    else call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}