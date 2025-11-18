package dev.koenv.libraryapi.server.routes.api.v1.auth

import dev.koenv.libraryapi.server.domain.service.AuthService
import dev.koenv.libraryapi.server.dto.auth.RegisterRequestDto
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

object RegisterRoutes {
    fun register(parent: Route) {
        val authService by parent.inject<AuthService>()

        parent.post("/register") {
            val req = call.receive<RegisterRequestDto>()
            runCatching { authService.register(req) }
                .onSuccess { call.respond(HttpStatusCode.Created, it) }
                .onFailure { call.respond(HttpStatusCode.Conflict, mapOf("error" to it.message)) }
        }
    }
}
