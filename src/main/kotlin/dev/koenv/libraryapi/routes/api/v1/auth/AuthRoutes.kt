package dev.koenv.libraryapi.routes.api.v1.auth

import dev.koenv.libraryapi.domain.service.AuthService
import dev.koenv.libraryapi.dto.auth.LoginRequestDto
import dev.koenv.libraryapi.dto.auth.RegisterRequestDto
import dev.koenv.libraryapi.routes.RouteRegistrar
import dev.koenv.libraryapi.shared.util.requireUser
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

object AuthRoutes : RouteRegistrar {
    override fun Route.register() {
        val authService by inject<AuthService>()

        route("/auth") {
            post("/register") {
                val req = call.receive<RegisterRequestDto>()
                runCatching { authService.register(req) }
                    .onSuccess { call.respond(HttpStatusCode.Created, it) }
                    .onFailure { call.respond(HttpStatusCode.Conflict, mapOf("error" to it.message)) }
            }

            post("/login") {
                val req = call.receive<LoginRequestDto>()
                runCatching { authService.login(req) }
                    .onSuccess { call.respond(HttpStatusCode.OK, it) }
                    .onFailure { call.respond(HttpStatusCode.Unauthorized, mapOf("error" to it.message)) }
            }

            authenticate("auth-jwt") {
                get("/me") {
                    val userId = call.requireUser()
                    val user = authService.getUserById(userId)
                    call.respond(HttpStatusCode.OK, user)
                }
            }

        }
    }
}