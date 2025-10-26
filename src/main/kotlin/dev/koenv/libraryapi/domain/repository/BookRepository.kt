package dev.koenv.libraryapi.domain.repository

import dev.koenv.libraryapi.domain.entity.Book
import java.util.*

interface BookRepository {
    suspend fun findAll(): List<Book>
    suspend fun findById(id: UUID): Book?
    suspend fun existsById(id: UUID): Boolean
    suspend fun findByIsbn(isbn: String): Book?

    suspend fun create(entity: Book): Book
    suspend fun update(id: UUID, entity: Book): Book?
    suspend fun delete(id: UUID): Boolean

    suspend fun count(): Long
}
