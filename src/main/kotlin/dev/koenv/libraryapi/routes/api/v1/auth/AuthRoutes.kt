package dev.koenv.libraryapi.routes.api.v1.auth

import dev.koenv.libraryapi.routes.RouteRegistrar
import io.ktor.server.routing.*

object AuthRoutes : RouteRegistrar {
    override fun Route.register() {
        route("/auth") {
            RegisterRoutes.register(this)
            SessionRoutes.register(this)
            TokenRoutes.register(this)
        }
    }
}
