package dev.koenv.libraryapi.shared.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.koenv.libraryapi.domain.entity.Role
import java.util.*

object JwtUtil {

    fun generateAccessToken(
        userId: UUID,
        sessionId: UUID,
        role: Role,
        audience: String,
        issuer: String,
        secret: String,
        expiresInSeconds: Long = 24 * 3600 // 1 day
    ): String {
        val now = System.currentTimeMillis()
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("sub", userId.toString())
            .withClaim("sid", sessionId.toString())
            .withClaim("role", role.name)
            .withExpiresAt(Date(now + expiresInSeconds * 1000))
            .sign(Algorithm.HMAC256(secret))
    }
}
