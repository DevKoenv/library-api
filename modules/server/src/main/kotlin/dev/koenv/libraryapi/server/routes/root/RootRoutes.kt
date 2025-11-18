package dev.koenv.libraryapi.server.routes.root

import dev.koenv.libraryapi.server.routes.RouteRegistrar
import io.ktor.server.response.*
import io.ktor.server.routing.*

object RootRoutes : RouteRegistrar {
    override fun Route.register() {
        route("/") {
            get("/health") { call.respondText("OK") }
        }
    }
}
