package dev.koenv.libraryapi.server.plugins

import dev.koenv.libraryapi.server.routes.registerAllRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        registerAllRoutes(this)
    }
}
