package dev.koenv.libraryapi.domain.service

import dev.koenv.libraryapi.domain.entity.Book
import dev.koenv.libraryapi.domain.entity.Loan
import dev.koenv.libraryapi.domain.repository.BookRepository
import dev.koenv.libraryapi.domain.repository.LoanRepository
import dev.koenv.libraryapi.domain.repository.UserRepository
import dev.koenv.libraryapi.dto.loan.CreateLoanDto
import dev.koenv.libraryapi.shared.http.ApiException
import io.ktor.http.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

class LoanService(
    private val repo: LoanRepository,
    private val users: UserRepository,
    private val books: BookRepository
) {
    suspend fun list(): List<Loan> = repo.findAll()

    suspend fun get(id: UUID): Loan =
        repo.findById(id) ?: throw ApiException(HttpStatusCode.NotFound, message = "Loan not found")

    suspend fun create(dto: CreateLoanDto): Loan {
        // Validate user
        val user = users.findById(dto.userId)
            ?: throw ApiException(HttpStatusCode.NotFound, message = "User not found")

        // Validate book and availability
        val book = books.findById(dto.bookId)
            ?: throw ApiException(HttpStatusCode.NotFound, message = "Book not found")

        if (book.copiesAvailable <= 0)
            throw ApiException(HttpStatusCode.Conflict, message = "No copies available")

        // Create loan, then decrement availability
        val created = repo.create(
            Loan(userId = user.id!!, bookId = book.id!!)
        )

        val updatedBook: Book = book.copy(copiesAvailable = book.copiesAvailable - 1)
        books.update(book.id!!, updatedBook)
            ?: throw ApiException(HttpStatusCode.InternalServerError, message = "Failed to adjust inventory")

        return created
    }

    suspend fun returnLoan(id: UUID): Loan {
        val loan = get(id)
        if (loan.returnedAt != null)
            throw ApiException(HttpStatusCode.Conflict, message = "Loan already returned")

        // Mark returned
        val returnedAt = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val impl = (repo as? dev.koenv.libraryapi.storage.repository.LoanRepositoryImpl)
            ?: throw ApiException(HttpStatusCode.InternalServerError, message = "Repository mismatch")
        val ok = impl.markReturned(id, returnedAt)
        if (!ok) throw ApiException(HttpStatusCode.InternalServerError, message = "Failed to update loan")

        // Increment availability
        val book = books.findById(loan.bookId)
            ?: throw ApiException(HttpStatusCode.InternalServerError, message = "Book missing")
        books.update(book.id!!, book.copy(copiesAvailable = book.copiesAvailable + 1))
            ?: throw ApiException(HttpStatusCode.InternalServerError, message = "Failed to restore inventory")

        return get(id)
    }

    suspend fun delete(id: UUID) {
        val loan = repo.findById(id)
            ?: throw ApiException(HttpStatusCode.NotFound, message = "Loan not found")

        // If deleting an active loan, restore availability
        if (loan.returnedAt == null) {
            val book = books.findById(loan.bookId)
                ?: throw ApiException(HttpStatusCode.InternalServerError, message = "Book missing")
            books.update(book.id!!, book.copy(copiesAvailable = book.copiesAvailable + 1))
                ?: throw ApiException(HttpStatusCode.InternalServerError, message = "Failed to restore inventory")
        }

        if (!repo.delete(id))
            throw ApiException(HttpStatusCode.InternalServerError, message = "Failed to delete loan")
    }
}
