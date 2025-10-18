package dev.koenv.libraryapi.routes.api.v1.auth

import dev.koenv.libraryapi.domain.service.SessionService
import dev.koenv.libraryapi.dto.auth.RefreshTokenRequestDto
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

object TokenRoutes {
    fun register(parent: Route) {
        val sessionService by parent.inject<SessionService>()

        parent.route("/tokens") {
            post("/refresh") {
                val req = call.receive<RefreshTokenRequestDto>()
                runCatching { sessionService.refresh(req.refreshToken) }
                    .onSuccess { call.respond(HttpStatusCode.OK, it) }
                    .onFailure { call.respond(HttpStatusCode.Unauthorized, mapOf("error" to it.message)) }
            }
        }
    }
}
