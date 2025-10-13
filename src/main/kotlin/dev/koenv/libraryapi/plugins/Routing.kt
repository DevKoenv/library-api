package dev.koenv.libraryapi.plugins

import dev.koenv.libraryapi.routes.registerAllRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        registerAllRoutes(this)
    }
}
