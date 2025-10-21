package dev.koenv.libraryapi.routes


import io.ktor.server.routing.*

sealed interface RouteNode

interface RouteRegistrar : RouteNode {
    fun Route.register()
}

data class RouteGroup(
    val prefix: String,
    val children: List<RouteNode>
) : RouteNode

fun registerAllRoutes(root: Route) {
    val tree: List<RouteNode> = listOf(
        RouteGroup("/", listOf(
            dev.koenv.libraryapi.routes.root.RootRoutes,
        )),
        RouteGroup("/api", listOf(
            RouteGroup("/v1", listOf(
                // All authentication routes
                dev.koenv.libraryapi.routes.api.v1.auth.AuthRoutes,

                // User CRUD
                dev.koenv.libraryapi.routes.api.v1.users.UserRoutes,

                // Book CRUD
                dev.koenv.libraryapi.routes.api.v1.books.BookRoutes,

                // Loans CRUD
//                dev.koenv.libraryapi.routes.api.v1.loans.LoanRoutes,
            ))
        ))
    )

    fun Route.registerNode(node: RouteNode) {
        when (node) {
            is RouteRegistrar -> with(node) { register() }
            is RouteGroup -> route(node.prefix) {
                node.children.forEach { child -> registerNode(child) }
            }
        }
    }

    tree.forEach { root.registerNode(it) }
}
