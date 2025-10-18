package dev.koenv.libraryapi.shared.http

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import java.util.*

object RequestUtil {

    fun ApplicationCall.requireUuidParam(name: String): UUID {
        val raw = parameters[name]
            ?: throw ApiException(HttpStatusCode.BadRequest, message = "Missing parameter '$name'")
        return runCatching { UUID.fromString(raw) }.getOrElse {
            throw ApiException(HttpStatusCode.BadRequest, message = "Invalid parameter '$name'")
        }
    }

    suspend inline fun <reified T : Any> ApplicationCall.requireBody(): T {
        return try {
            receive()
        } catch (_: Exception) {
            throw ApiException(HttpStatusCode.BadRequest, message = "Invalid request body")
        }
    }
}
