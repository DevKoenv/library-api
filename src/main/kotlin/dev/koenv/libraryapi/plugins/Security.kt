package dev.koenv.libraryapi.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.koenv.libraryapi.domain.repository.UserSessionRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject
import java.util.UUID

fun Application.configureSecurity() {
    val sessionRepo by inject<UserSessionRepository>()

    val config = environment.config
    val jwtAudience = config.property("jwt.audience").getString()
    val jwtDomain = config.property("jwt.domain").getString()
    val jwtRealm = config.property("jwt.realm").getString()
    val jwtSecret = config.property("jwt.secret").getString()

    authentication {
        jwt("auth-jwt") {
            realm = jwtRealm
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build()
            )
            validate { cred ->
                val sid = cred.payload.getClaim("sid").asString()
                val sub = cred.payload.getClaim("sub").asString()
                if (sid == null || sub == null) return@validate null

                // check revoked
                val session = sessionRepo.findById(UUID.fromString(sid))
                if (session == null || session.revokedAt != null) return@validate null

                JWTPrincipal(cred.payload)
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
}
