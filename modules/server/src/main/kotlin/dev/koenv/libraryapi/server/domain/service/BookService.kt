package dev.koenv.libraryapi.server.domain.service

import dev.koenv.libraryapi.server.domain.entity.Book
import dev.koenv.libraryapi.server.domain.repository.BookRepository
import dev.koenv.libraryapi.server.dto.book.CreateBookDto
import dev.koenv.libraryapi.server.dto.book.UpdateBookDto
import dev.koenv.libraryapi.server.mappers.book.toEntity
import dev.koenv.libraryapi.server.mappers.book.applyTo
import dev.koenv.libraryapi.server.shared.http.ApiException
import io.ktor.http.*
import java.util.UUID

class BookService(private val repo: BookRepository) {

    suspend fun list(): List<Book> = repo.findAll()

    suspend fun get(id: UUID): Book =
        repo.findById(id) ?: throw ApiException(HttpStatusCode.NotFound, message = "Book not found")

    suspend fun create(dto: CreateBookDto): Book {
        if (repo.findByIsbn(dto.isbn) != null)
            throw ApiException(HttpStatusCode.Conflict, message = "Book with same ISBN already exists")
        return repo.create(dto.toEntity())
    }

    suspend fun update(id: UUID, dto: UpdateBookDto): Book {
        val existing = get(id)
        val updated = dto.applyTo(existing)
        return repo.update(id, updated)
            ?: throw ApiException(HttpStatusCode.InternalServerError, message = "Failed to update book")
    }

    suspend fun delete(id: UUID) {
        if (!repo.delete(id))
            throw ApiException(HttpStatusCode.NotFound, message = "Book not found")
    }
}
