package dev.koenv.libraryapi.routes.api.v1.loans

import dev.koenv.libraryapi.domain.policy.LoanPolicies.requireCanBorrowBook
import dev.koenv.libraryapi.domain.policy.LoanPolicies.requireCanDeleteLoan
import dev.koenv.libraryapi.domain.policy.LoanPolicies.requireCanListLoans
import dev.koenv.libraryapi.domain.policy.LoanPolicies.requireCanReturnLoan
import dev.koenv.libraryapi.domain.service.LoanService
import dev.koenv.libraryapi.dto.loan.CreateLoanDto
import dev.koenv.libraryapi.dto.loan.ReturnLoanDto
import dev.koenv.libraryapi.mappers.loan.toDto
import dev.koenv.libraryapi.routes.RouteRegistrar
import dev.koenv.libraryapi.shared.auth.AuthContext.requireUser
import dev.koenv.libraryapi.shared.http.RequestUtil.requireBody
import dev.koenv.libraryapi.shared.http.RequestUtil.requireUuidParam
import io.ktor.http.*
import io.ktor.server.auth.authenticate
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

object LoanRoutes : RouteRegistrar {
    override fun Route.register() {
        val service by inject<LoanService>()

        route("/loans") {
            authenticate("auth-jwt") {

                // List loans
                get {
                    val showAll = call.request.queryParameters["all"]?.toBooleanStrictOrNull() == true
                    call.requireCanListLoans(showAll)
                    val items = service.list()
                    // If not showAll, filter to current user
                    val out = if (showAll) items else {
                        val me = call.requireUser()
                        items.filter { it.userId == me }
                    }
                    call.respond(HttpStatusCode.OK, out.map { it.toDto() })
                }

                // Get by id
                get("/{id}") {
                    val id = call.requireUuidParam("id")
                    val loan = service.get(id)
                    // Permission depends on ownership; policy uses targetUserId
                    call.requireCanReturnLoan(loan.userId) // read permission piggy-backed on same rule set
                    call.respond(HttpStatusCode.OK, loan.toDto())
                }

                // Create loan
                post {
                    call.requireCanBorrowBook()
                    val dto = call.requireBody<CreateLoanDto>()
                    val created = service.create(dto)
                    call.respond(HttpStatusCode.Created, created.toDto())
                }

                // Return loan
                patch("/{id}/return") {
                    val id = call.requireUuidParam("id")
                    val body = call.requireBody<ReturnLoanDto>()
                    if (!body.confirm) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@patch
                    }
                    val loan = service.get(id)
                    call.requireCanReturnLoan(loan.userId)
                    val updated = service.returnLoan(id)
                    call.respond(HttpStatusCode.OK, updated.toDto())
                }

                // Delete loan
                delete("/{id}") {
                    val id = call.requireUuidParam("id")
                    val loan = service.get(id)
                    call.requireCanDeleteLoan(loan.userId)
                    service.delete(id)
                    call.respond(HttpStatusCode.NoContent)
                }
            }
        }
    }
}
