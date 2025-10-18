package dev.koenv.libraryapi.routes.api.v1.auth

import dev.koenv.libraryapi.domain.service.AuthService
import dev.koenv.libraryapi.domain.service.SessionService
import dev.koenv.libraryapi.dto.auth.AuthResponseDto
import dev.koenv.libraryapi.dto.auth.LoginRequestDto
import dev.koenv.libraryapi.mappers.user.toDto
import dev.koenv.libraryapi.shared.auth.AuthContext.requireUser
import dev.koenv.libraryapi.shared.http.RequestUtil.requireUuidParam
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

object SessionRoutes {
    fun register(parent: Route) {
        val authService by parent.inject<AuthService>()
        val sessionService by parent.inject<SessionService>()

        parent.route("/sessions") {
            // Login → issue access + refresh
            post {
                val req = call.receive<LoginRequestDto>()
                runCatching {
                    val user = authService.authenticate(req.email, req.password)
                    val pair = sessionService.createSession(user)
                    AuthResponseDto(pair.accessToken, pair.refreshToken, user.toDto())
                }
                    .onSuccess { call.respond(HttpStatusCode.OK, it) }
                    .onFailure { call.respond(HttpStatusCode.Unauthorized, mapOf("error" to it.message)) }
            }

            authenticate("auth-jwt") {
                // List sessions for current user
                get {
                    val userId = call.requireUser()
                    val sessions = sessionService.listSessions(userId)
                    call.respond(HttpStatusCode.OK, sessions)
                }

                // Logout current session
                delete {
                    val userId = call.requireUser()
                    val sid = call.principal<JWTPrincipal>()?.payload?.getClaim("sid")?.asString()
                    val sessionId = runCatching { sid?.let(UUID::fromString) }.getOrNull()
                    sessionService.logoutCurrent(sessionId, userId)
                    call.respond(HttpStatusCode.NoContent)
                }

                // Invalidate specific session by id
                delete("{id}") {
                    val userId = call.requireUser()
                    val targetId = call.requireUuidParam("id")
                    sessionService.logoutById(targetId, userId)
                    call.respond(HttpStatusCode.NoContent)
                }

                // Logout all sessions for user
                delete("all") {
                    val userId = call.requireUser()
                    sessionService.logoutAll(userId)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}
