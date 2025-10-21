package dev.koenv.libraryapi.routes.api.v1.books

import dev.koenv.libraryapi.domain.policy.BookPolicies.requireCanCreateBook
import dev.koenv.libraryapi.domain.policy.BookPolicies.requireCanDeleteBook
import dev.koenv.libraryapi.domain.policy.BookPolicies.requireCanListBooks
import dev.koenv.libraryapi.domain.policy.BookPolicies.requireCanUpdateBook
import dev.koenv.libraryapi.domain.service.BookService
import dev.koenv.libraryapi.dto.book.CreateBookDto
import dev.koenv.libraryapi.dto.book.UpdateBookDto
import dev.koenv.libraryapi.mappers.book.toDto
import dev.koenv.libraryapi.routes.RouteRegistrar
import dev.koenv.libraryapi.shared.http.RequestUtil.requireBody
import dev.koenv.libraryapi.shared.http.RequestUtil.requireUuidParam
import io.ktor.http.*
import io.ktor.server.auth.authenticate
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

object BookRoutes : RouteRegistrar {
    override fun Route.register() {
        val service by inject<BookService>()

        route("/books") {
            authenticate("auth-jwt") {

                // List all books
                get {
                    call.requireCanListBooks()
                    call.respond(HttpStatusCode.OK, service.list().map { it.toDto() })
                }

                // Create a book
                post {
                    call.requireCanCreateBook()
                    val dto = call.requireBody<CreateBookDto>()
                    val created = service.create(dto)
                    call.respond(HttpStatusCode.Created, created.toDto())
                }

                // Get by id
                get("/{id}") {
                    call.requireCanListBooks()
                    val id = call.requireUuidParam("id")
                    call.respond(HttpStatusCode.OK, service.get(id).toDto())
                }

                // Update book
                put("/{id}") {
                    call.requireCanUpdateBook()
                    val id = call.requireUuidParam("id")
                    val dto = call.requireBody<UpdateBookDto>()
                    val updated = service.update(id, dto)
                    call.respond(HttpStatusCode.OK, updated.toDto())
                }

                // Delete book
                delete("/{id}") {
                    call.requireCanDeleteBook()
                    val id = call.requireUuidParam("id")
                    service.delete(id)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}
