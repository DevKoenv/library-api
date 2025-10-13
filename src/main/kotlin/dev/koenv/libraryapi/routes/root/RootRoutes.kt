package dev.koenv.libraryapi.routes.root

import dev.koenv.libraryapi.routes.RouteRegistrar
import io.ktor.server.response.*
import io.ktor.server.routing.*

object RootRoutes : RouteRegistrar {
    override fun Route.register() {
        route("/") {
            get("/health") { call.respondText("OK") }
        }
    }
}
