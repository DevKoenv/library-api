package dev.koenv.libraryapi.domain.policy

import dev.koenv.libraryapi.enums.Permission
import dev.koenv.libraryapi.shared.auth.AuthContext.hasPermission
import dev.koenv.libraryapi.shared.auth.AuthContext.requirePermission
import dev.koenv.libraryapi.shared.auth.AuthContext.requireUser
import dev.koenv.libraryapi.shared.http.ApiException
import io.ktor.http.*
import io.ktor.server.application.*
import java.util.*

object LoanPolicies {

    fun ApplicationCall.requireCanListLoans(showAll: Boolean) {
        if (showAll) requirePermission(Permission.LOAN_READ_OTHERS)
        else requirePermission(Permission.LOAN_READ_SELF)
    }

    fun ApplicationCall.requireCanBorrowBook() =
        requirePermission(Permission.LOAN_CREATE)

    fun ApplicationCall.requireCanReturnLoan(targetUserId: UUID) {
        val currentId = requireUser()
        val same = currentId == targetUserId
        val allowed = when {
            same -> hasPermission(Permission.LOAN_UPDATE_SELF)
            else -> hasPermission(Permission.LOAN_UPDATE_OTHERS)
        }
        if (!allowed)
            throw ApiException(HttpStatusCode.Forbidden, message = "Not allowed to update this loan")
    }

    fun ApplicationCall.requireCanDeleteLoan(targetUserId: UUID) {
        val currentId = requireUser()
        val same = currentId == targetUserId
        val allowed = when {
            same -> hasPermission(Permission.LOAN_DELETE_SELF)
            else -> hasPermission(Permission.LOAN_DELETE_OTHERS)
        }
        if (!allowed)
            throw ApiException(HttpStatusCode.Forbidden, message = "Not allowed to delete this loan")
    }
}
